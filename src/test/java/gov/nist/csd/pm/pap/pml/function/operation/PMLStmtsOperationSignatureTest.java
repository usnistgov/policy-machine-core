package gov.nist.csd.pm.pap.pml.function.operation;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PMLStmtsOperationSignatureTest {

    @Test
    void testToFormattedString() {
        PMLOperationSignature pmlStmtsOperationSignature = new PMLOperationSignature(
                "op1",
                STRING_TYPE,
            List.of(
                new PMLNodeFormalArg("a", STRING_TYPE),
                new PMLFormalArg("b", STRING_TYPE),
                new PMLFormalArg("c", STRING_TYPE)
            )
        );

        assertEquals(
                "operation op1(@node string a, string b, string c) string ",
                pmlStmtsOperationSignature.toFormattedString(0)
        );
    }

}