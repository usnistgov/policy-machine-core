package gov.nist.csd.pm.core.pap.pml.operation.admin;


import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import java.util.List;
import org.junit.jupiter.api.Test;

class PMLStmtsOperationSignatureTest {

    @Test
    void testToFormattedString() {
        PMLOperationSignature pmlStmtsOperationSignature = new PMLOperationSignature(
            OperationType.ADMINOP,
            "op1",
            STRING_TYPE,
            List.of(
                new NodeNameFormalParameter("a"),
                new FormalParameter<>("b", STRING_TYPE),
                new FormalParameter<>("c", STRING_TYPE)
            )
        );

        assertEquals(
            "adminop op1(@node string a, string b, string c) string ",
            pmlStmtsOperationSignature.toFormattedString(0)
        );
    }

}