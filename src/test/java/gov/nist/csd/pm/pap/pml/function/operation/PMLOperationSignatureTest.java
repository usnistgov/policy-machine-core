package gov.nist.csd.pm.pap.pml.function.operation;

import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PMLOperationSignatureTest {

    @Test
    void testToFormattedString() {
        PMLOperationSignature signature = new PMLOperationSignature(
                "op1",
                Type.array(Type.string()),
                List.of(
                    new PMLNodeFormalArg("a", Type.string()),
                    new PMLFormalArg("b", Type.string()),
                    new PMLFormalArg("c", Type.string())
                )
        );

        String actual = signature.toFormattedString(0);
        assertEquals(
                "operation op1(@node string a, string b, string c) []string ",
                actual
        );
    }

}