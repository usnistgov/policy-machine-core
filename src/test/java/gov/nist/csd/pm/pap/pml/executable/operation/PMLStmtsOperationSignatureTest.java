package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PMLStmtsOperationSignatureTest {

    @Test
    void testToFormattedString() {
        PMLOperationSignature pmlStmtsOperationSignature = new PMLOperationSignature(
                "op1",
                Type.string(),
                List.of("a", "b", "c"),
                List.of("a"),
                Map.of("a", Type.array(Type.string()), "b", Type.string(), "c", Type.string())
        );

        assertEquals(
                "operation op1(@node []string a, string b, string c) string ",
                pmlStmtsOperationSignature.toFormattedString(0)
        );
    }

}