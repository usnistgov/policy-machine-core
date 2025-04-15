package gov.nist.csd.pm.pap.pml.function.operation;


import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.arg.NodeFormalParameter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.listType;
import static org.junit.jupiter.api.Assertions.*;

class PMLOperationSignatureTest {

    @Test
    void testToFormattedString() {
        PMLOperationSignature signature = new PMLOperationSignature(
                "op1",
                listType(STRING_TYPE),
                List.of(
                    new NodeFormalParameter<>("a", STRING_TYPE),
                    new FormalParameter<>("b", STRING_TYPE),
                    new FormalParameter<>("c", STRING_TYPE)
                )
        );

        String actual = signature.toFormattedString(0);
        assertEquals(
                "operation op1(@node string a, string b, string c) []string ",
                actual
        );
    }

}