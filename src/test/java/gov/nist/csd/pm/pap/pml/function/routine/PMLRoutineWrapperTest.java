package gov.nist.csd.pm.pap.pml.function.routine;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.function.arg.type.SupportedArgTypes.stringType;
import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PMLRoutineWrapperTest {

    private static final FormalArg<String> a = new FormalArg<>("a", stringType());
    private static final FormalArg<String> b = new FormalArg<>("b", stringType());

    @Test
    void testConstructor() {
        Routine<?> op = new Routine<>("routine1", List.of(a, b)) {

            @Override
            public Object execute(PAP pap, Args actualArgs) throws PMException {
                return null;
            }
        };

        PMLRoutine pmlRoutineWrapper = new PMLRoutineWrapper(op);
        assertEquals(
            pmlRoutineWrapper.getSignature(),
            new PMLRoutineSignature("routine1", Type.any(), List.of(
                new PMLFormalArg("a", Type.any()), new PMLFormalArg("b", Type.any())
            ))
        );
    }

    @Test
    void testExecuteWithPDP() throws PMException {
        Routine<?> op = new Routine<>("routine1", List.of(a, b)) {

            @Override
            public Object execute(PAP pap, Args operands) throws PMException {
                pap.modify().graph().createObjectAttribute(operands.get(a), List.of(id("pc1")));
                pap.modify().graph().createObjectAttribute(operands.get(b), List.of(id("pc1")));
                return null;
            }
        };

        String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["assign"]
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]
                create u "u1" in ["ua1"]
                """;
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), pml);

        pap.modify().routines().createAdminRoutine(op);

        PDP pdp = new PDP(pap);
        pdp.adjudicateAdminRoutine(
            new TestUserContext("u1"),
            pap.query().routines().getAdminRoutine("routine1"),
            new Args().put(a, "a").put(b, "b")
        );
        assertTrue(pap.query().graph().nodeExists("a"));
        assertTrue(pap.query().graph().nodeExists("b"));

        // try again using pml
        pap.reset();
        pdp = new PDP(pap);
        pap.executePML(new TestUserContext("u1"), pml);
        pap.modify().routines().createAdminRoutine(op);
        pdp.runTx(new TestUserContext("u1"), tx -> {
            tx.executePML(new TestUserContext("u1"), "routine1(\"a\", \"b\")");
            return null;
        });
        assertTrue(pap.query().graph().nodeExists("a"));
        assertTrue(pap.query().graph().nodeExists("b"));
    }

    @Test
    void testPMLRoutineWrapperWithReturnValue() throws PMException {
        Routine<?> op = new Routine<>("routine1", List.of()) {

            @Override
            public String execute(PAP pap, Args actualArgs) throws PMException {
                return "test";
            }
        };

        MemoryPAP pap = new TestPAP();

        pap.modify().routines().createAdminRoutine(new PMLRoutineWrapper(op));
        pap.executePML(new TestUserContext("u1"), "create policy class routine1()");
        assertTrue(pap.query().graph().nodeExists("test"));
    }

}