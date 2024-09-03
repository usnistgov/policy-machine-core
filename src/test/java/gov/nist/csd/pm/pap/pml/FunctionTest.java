package gov.nist.csd.pm.pap.pml;

import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.pap.query.UserContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FunctionTest {

    @Test
    void testElseIfNotAllPathsReturn() {
        String pml = "operation fun(string a) string {\n" +
                "                    if equals(a, \"a\") {\n" +
                "                        return \"a\"\n" +
                "                    } else if equals(a, \"b\") {\n" +
                "                        return \"b\"\n" +
                "                    }\n" +
                "                }";

        PMLCompilationException e = assertThrows(PMLCompilationException.class, () -> {
            PAP pap = new MemoryPAP();
            pap.executePML(new UserContext("u1"), pml);
        });
        assertEquals("not all conditional paths return", e.getErrors().get(0).errorMessage());
    }

    @Test
    void testElseAllPathsReturn() {
        String pml2 = "operation fun(string a) string {\n" +
                "                    if equals(a, \"a\") {\n" +
                "                        return \"a\"\n" +
                "                    } else if equals(a, \"b\") {\n" +
                "                        return \"b\"\n" +
                "                    } else {\n" +
                "                        return \"c\"\n" +
                "                    }\n" +
                "                }";

        assertDoesNotThrow(() -> {
            PAP pap = new MemoryPAP();
            pap.executePML(new UserContext("u1"), pml2);
        });
    }

    @Test
    void testElseWithNoElseIfAllPathsReturn() {
        String pml2 = "operation fun(string a) string {\n" +
                "                    if equals(a, \"a\") {\n" +
                "                        return \"a\"\n" +
                "                    } else {\n" +
                "                        return \"b\"\n" +
                "                    }\n" +
                "                }";

        assertDoesNotThrow(() -> {
            PAP pap = new MemoryPAP();
            pap.executePML(new UserContext("u1"), pml2);
        });
    }

}
