package gov.nist.csd.pm.pap.pml.function.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.op.arg.IdNodeFormalArg;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.List;


import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.*;

class PMLOperationWrapperTest {

    private static final IdNodeFormalArg a = new IdNodeFormalArg("a");
    private static final FormalArg<String> b = new FormalArg<>("b", String.class);

    @Test
    void testConstructor() {
        Operation<?> op = new Operation<>("op1", List.of(a, b)) {

            @Override
            public Object execute(PAP pap, ActualArgs actualArgs) {
                return null;
            }

            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs operands) {

            }
        };

        PMLOperationWrapper pmlOperationWrapper = new PMLOperationWrapper(op);
        assertEquals(
            pmlOperationWrapper.getSignature(),
            new PMLOperationSignature(
                "op1",
                Type.any(),
                List.of(new PMLNodeFormalArg("a", Type.any()), new PMLFormalArg("b", Type.any())))
        );
    }

    @Test
    void testExecuteWithPDP() throws PMException {
        Operation<Object> op = new Operation<>("op1", List.of(a, b)) {

            @Override
            public Object execute(PAP pap, ActualArgs actualArgs) throws PMException {
                pap.modify().graph().createPolicyClass(actualArgs.get(b));
                return null;
            }

            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs operands) throws PMException {
                privilegeChecker.check(userCtx, operands.get(a), List.of("assign"));
            }
        };

        String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["assign"]
                create u "u1" in ["ua1"]
                """;
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), pml);

        pap.modify().operations().createAdminOperation(new PMLOperationWrapper(op));

        PDP pdp = new PDP(pap);
        pdp.adjudicateAdminOperation(
            new TestUserContext("u1"),
            op,
            new ActualArgs().put(a, id("oa1")).put(b, "b"));
        assertTrue(pap.query().graph().nodeExists("b"));

        // try again using pml
        pap.reset();
        pdp = new PDP(pap);
        pap.executePML(new TestUserContext("u1"), pml);
        pap.modify().operations().createAdminOperation(new PMLOperationWrapper(op));
        pdp.runTx(new TestUserContext("u1"), tx -> {
            tx.executePML(new TestUserContext("u1"), "op1(\"oa1\", \"b\", \"c\")");
            return null;
        });
        assertTrue(pap.query().graph().nodeExists("b"));
    }

    @Test
    void testPMLOperationWrapperWithReturnValue() throws PMException {
        Operation<?> op = new Operation<>("op1", List.of()) {

            @Override
            public Object execute(PAP pap, ActualArgs actualArgs) throws PMException {
                return "test";
            }

            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs operands) throws PMException {
                privilegeChecker.check(userCtx, operands.get(a), List.of("assign"));
            }
        };

        MemoryPAP pap = new TestPAP();

        pap.modify().operations().createAdminOperation(new PMLOperationWrapper(op));
        pap.executePML(new TestUserContext("u1"), "create policy class op1()");
        assertTrue(pap.query().graph().nodeExists("test"));
    }
}