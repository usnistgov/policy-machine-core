package gov.nist.csd.pm.core.pap.pml.operation.admin;


import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import java.util.List;
import org.junit.jupiter.api.Test;

class PMLAdminOperationSignatureTest {

    @Test
    void testToFormattedString() {
        PMLOperationSignature signature = new PMLOperationSignature(
            OperationType.ADMINOP,
            "op1",
            ListType.of(STRING_TYPE),
            List.of(
                new NodeNameFormalParameter("a"),
                new FormalParameter<>("b", STRING_TYPE),
                new FormalParameter<>("c", STRING_TYPE)
            )
        );

        String actual = signature.toFormattedString(0);
        assertEquals(
            "adminop op1(@node string a, string b, string c) []string ",
            actual
        );
    }

}