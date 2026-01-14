package gov.nist.csd.pm.core.pap.pml;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.ResourceOperation;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLResourceOperation;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.*;

public class FunctionTest {

    @Test
    void testElseIfNotAllPathsReturn() {
        String pml = """
                adminop fun(string a) string {
                    if a == "a" {
                        return "a"
                    } else if a == "b" {
                        return "b"
                    }
                }
                """;

        PMLCompilationException e = assertThrows(PMLCompilationException.class, () -> {
            PAP pap = new TestPAP();
            pap.executePML(new TestUserContext("u1"), pml);
        });
        assertEquals("not all conditional paths return", e.getErrors().get(0).errorMessage());
    }

    @Test
    void testElseAllPathsReturn() {
        String pml2 = """
                adminop fun(string a) string {
                    if a == "a" {
                        return "a"
                    } else if a == "b" {
                        return "b"
                    } else {
                        return "c"
                    }
                }
                """;

        assertDoesNotThrow(() -> {
            PAP pap = new TestPAP();
            pap.executePML(new TestUserContext("u1"), pml2);
        });
    }

    @Test
    void testElseWithNoElseIfAllPathsReturn() {
        String pml2 = """
                adminop fun(string a) string {
                    if a == "a" {
                        return "a"
                    } else {
                        return "b"
                    }
                }
                """;

        assertDoesNotThrow(() -> {
            PAP pap = new TestPAP();
            pap.executePML(new TestUserContext("u1"), pml2);
        });
    }

    @Test
    void testResourceOperationCheckBlock() throws PMException {
        String pml = """
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create u "u1" in ["ua1"]
            
            resourceop res1() {
                check ["read"] on [PM_ADMIN_BASE_OA]
            }
            """;

        MemoryPAP memoryPAP = new TestPAP();
        memoryPAP.executePML(new UserContext(-1), pml);

        PMLResourceOperation res1 = (PMLResourceOperation) memoryPAP.query().operations().getResourceOperation("res1");
        ExecutionContext u1 = memoryPAP.buildExecutionContext(new UserContext(id("u1")));
        res1.setCtx(u1);
        assertThrows(UnauthorizedException.class, () -> res1.canExecute(memoryPAP, new UserContext(id("u1")), new Args()));
    }

}
