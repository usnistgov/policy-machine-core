package gov.nist.csd.pm.pap.pml;

import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FunctionTest {

    @Test
    void testElseIfNotAllPathsReturn() {
        String pml = """
                operation fun(string a) string {
                    if equals(a, "a") {
                        return "a"
                    } else if equals(a, "b") {
                        return "b"
                    }
                }
                """;

        PMLCompilationException e = assertThrows(PMLCompilationException.class, () -> {
            PAP pap = new MemoryPAP();
            pap.executePML(new UserContext("u1"), pml);
        });
        assertEquals("not all conditional paths return", e.getErrors().get(0).errorMessage());
    }

    @Test
    void testElseAllPathsReturn() {
        String pml2 = """
                operation fun(string a) string {
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
            PAP pap = new MemoryPAP();
            pap.executePML(new UserContext("u1"), pml2);
        });
    }

    @Test
    void testElseWithNoElseIfAllPathsReturn() {
        String pml2 = """
                operation fun(string a) string {
                    if equals(a, "a") {
                        return "a"
                    } else {
                        return "b"
                    }
                }
                """;

        assertDoesNotThrow(() -> {
            PAP pap = new MemoryPAP();
            pap.executePML(new UserContext("u1"), pml2);
        });
    }

}
