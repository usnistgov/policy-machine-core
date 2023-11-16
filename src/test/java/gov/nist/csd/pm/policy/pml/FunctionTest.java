package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.exception.PMLCompilationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FunctionTest {

    @Test
    void testElseIfNotAllPathsReturn() {
        String pml = """
                function fun(string a) string {
                    if equals(a, "a") {
                        return "a"
                    } else if equals(a, "b") {
                        return "b"
                    }
                }
                """;

        PMLCompilationException e = assertThrows(PMLCompilationException.class, () -> {
            PAP pap = new PAP(new MemoryPolicyStore());
            PMLExecutor.compileAndExecutePML(pap, new UserContext("u1"), pml);
        });
        assertEquals("not all conditional paths return", e.getErrors().get(0).errorMessage());
    }

    @Test
    void testElseAllPathsReturn() {
        String pml2 = """
                function fun(string a) string {
                    if equals(a, "a") {
                        return "a"
                    } else if equals(a, "b") {
                        return "b"
                    } else {
                        return "c"
                    }
                }
                """;

        assertDoesNotThrow(() -> {
            PAP pap = new PAP(new MemoryPolicyStore());
            PMLExecutor.compileAndExecutePML(pap, new UserContext("u1"), pml2);
        });
    }

    @Test
    void testElseWithNoElseIfAllPathsReturn() {
        String pml2 = """
                function fun(string a) string {
                    if equals(a, "a") {
                        return "a"
                    } else {
                        return "b"
                    }
                }
                """;

        assertDoesNotThrow(() -> {
            PAP pap = new PAP(new MemoryPolicyStore());
            PMLExecutor.compileAndExecutePML(pap, new UserContext("u1"), pml2);
        });
    }

}
