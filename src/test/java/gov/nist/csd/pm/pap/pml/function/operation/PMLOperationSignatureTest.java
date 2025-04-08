package gov.nist.csd.pm.pap.pml.function.operation;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PMLOperationSignatureTest {

    @Test
    void testToFormattedString() {
        PMLOperationSignature signature = new PMLOperationSignature(
                "op1",
                listType(STRING_TYPE),
                List.of(
                    new PMLNodeFormalArg("a", STRING_TYPE),
                    new PMLFormalArg("b", STRING_TYPE),
                    new PMLFormalArg("c", STRING_TYPE)
                )
        );

        String actual = signature.toFormattedString(0);
        assertEquals(
                "operation op1(@node string a, string b, string c) []string ",
                actual
        );
    }

}