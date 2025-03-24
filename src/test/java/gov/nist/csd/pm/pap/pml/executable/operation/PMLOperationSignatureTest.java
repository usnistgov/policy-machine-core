package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PMLOperationSignatureTest {

    @Test
    void testToFormattedString() {
        PMLOperationSignature signature = new PMLOperationSignature(
                "op1",
                Type.array(Type.string()),
                List.of("a", "b", "c"),
                List.of("a"),
                Map.of("a", Type.string(), "b", Type.string(), "c", Type.string())
        );

        String actual = signature.toFormattedString(0);
        assertEquals(
                "operation op1(@node string a, string b, string c) []string ",
                actual
        );
    }

}