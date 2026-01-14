package gov.nist.csd.pm.core.pap.pml.function.operation;


import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeFormalParameter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import static org.junit.jupiter.api.Assertions.*;

class PMLAdminOperationSignatureTest {

    @Test
    void testToFormattedString() {
        PMLOperationSignature signature = new PMLOperationSignature(
                "op1",
                ListType.of(STRING_TYPE),
                List.of(
                    new NodeFormalParameter("a"),
                    new FormalParameter<>("b", STRING_TYPE),
                    new FormalParameter<>("c", STRING_TYPE)
                ),
            true
        );

        String actual = signature.toFormattedString(0);
        assertEquals(
                "adminop op1(node string a, string b, string c) []string ",
                actual
        );
    }

}