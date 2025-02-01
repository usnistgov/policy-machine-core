package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

class PMLOperationWrapperTest {

    @Test
    void testConstructor() {
        Operation<?> op = new Operation<>("op1", List.of("a", "b", "c"), List.of("a")) {

            @Override
            public Object execute(PAP pap, Map operands) throws PMException {
                return null;
            }

            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map operands) throws PMException {

            }
        };

        PMLOperationWrapper pmlOperationWrapper = new PMLOperationWrapper(op);
        assertEquals(
                pmlOperationWrapper.getSignature(),
                new PMLExecutableSignature("op1", Type.any(), List.of("a", "b", "c"),
                        Map.of("a", Type.any(), "b", Type.any(), "c", Type.any()))
        );
    }

    @Test
    void testExecuteWithPDP() throws PMException {
        Operation<?> op = new Operation<>("op1", List.of("a", "b", "c"), List.of("a")) {

            @Override
            public Object execute(PAP pap, Map<String, Object> operands) throws PMException {
                pap.modify().graph().createPolicyClass((String) operands.get("b"));
                return null;
            }

            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
                privilegeChecker.check(userCtx, (String) operands.get("a"), List.of("assign"));
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
        pdp.adjudicateAdminOperation(new TestUserContext("u1"), "op1",
                Map.of("a", "oa1", "b", "b", "c", "c"));
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
        Operation<?> op = new Operation<>("op1", List.of(), List.of()) {

            @Override
            public Object execute(PAP pap, Map<String, Object> operands) throws PMException {
                return "test";
            }

            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
                privilegeChecker.check(userCtx, (String) operands.get("a"), List.of("assign"));
            }
        };

        MemoryPAP pap = new TestPAP();

        pap.modify().operations().createAdminOperation(new PMLOperationWrapper(op));
        pap.executePML(new TestUserContext("u1"), "create policy class op1()");
        assertTrue(pap.query().graph().nodeExists("test"));
    }
}