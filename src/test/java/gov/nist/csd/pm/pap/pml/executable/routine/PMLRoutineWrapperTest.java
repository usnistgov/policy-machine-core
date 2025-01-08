package gov.nist.csd.pm.pap.pml.executable.routine;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.common.routine.Routine;
import gov.nist.csd.pm.pdp.PDP;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PMLRoutineWrapperTest {

    @Test
    void testConstructor() {
        Routine<?> op = new Routine<>("routine1", List.of("a", "b", "c")) {

            @Override
            public Object execute(PAP pap, Map<String, Object> operands) throws PMException {
                return null;
            }
        };

        PMLRoutine pmlRoutineWrapper = new PMLRoutineWrapper(op);
        assertEquals(
                pmlRoutineWrapper.getSignature(),
                new PMLExecutableSignature("routine1", Type.any(), List.of("a", "b", "c"),
                        Map.of("a", Type.any(), "b", Type.any(), "c", Type.any()))
        );
    }
    
    @Test
    void testExecuteWithPDP() throws PMException {
        Routine<?> op = new Routine<Object>("routine1", List.of("a", "b", "c")) {

            @Override
            public Object execute(PAP pap, Map<String, Object> operands) throws PMException {
                pap.modify().graph().createObjectAttribute((String) operands.get("a"), List.of("pc1"));
                pap.modify().graph().createObjectAttribute((String) operands.get("b"), List.of("pc1"));
                pap.modify().graph().createObjectAttribute((String) operands.get("c"), List.of("pc1"));
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
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), pml);

        pap.modify().routines().createAdminRoutine(new PMLRoutineWrapper(op));

        PDP pdp = new PDP(pap);
        pdp.adjudicateAdminRoutine(new UserContext("u1"), "routine1",
                Map.of("a", "a", "b", "b", "c", "c"));
        assertTrue(pap.query().graph().nodeExists("a"));
        assertTrue(pap.query().graph().nodeExists("b"));
        assertTrue(pap.query().graph().nodeExists("c"));

        // try again using pml
        pap = new MemoryPAP();
        pdp = new PDP(pap);
        pap.executePML(new UserContext("u1"), pml);
        pap.modify().routines().createAdminRoutine(new PMLRoutineWrapper(op));
        pdp.runTx(new UserContext("u1"), tx -> {
            tx.executePML(new UserContext("u1"), "routine1(\"a\", \"b\", \"c\")");
            return null;
        });
        assertTrue(pap.query().graph().nodeExists("a"));
        assertTrue(pap.query().graph().nodeExists("b"));
        assertTrue(pap.query().graph().nodeExists("c"));
    }

    @Test
    void testPMLRoutineWrapperWithReturnValue() throws PMException {
        Routine<?> op = new Routine<>("routine1", List.of()) {

            @Override
            public String execute(PAP pap, Map<String, Object> operands) throws PMException {
                return "test";
            }
        };

        MemoryPAP pap = new MemoryPAP();

        pap.modify().routines().createAdminRoutine(new PMLRoutineWrapper(op));
        pap.executePML(new UserContext("u1"), "create policy class routine1()");
        assertTrue(pap.query().graph().nodeExists("test"));
    }

}