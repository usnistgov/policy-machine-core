package gov.nist.csd.pm.core.pap.pml.function.operation;


import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeNameFormalParameter;
import java.util.List;
import org.junit.jupiter.api.Test;

class PMLStmtsOperationSignatureTest {

    @Test
    void testToFormattedString() {
        PMLOperationSignature pmlStmtsOperationSignature = new PMLOperationSignature(
                "op1",
                STRING_TYPE,
            List.of(
                new NodeNameFormalParameter("a"),
                new FormalParameter<>("b", STRING_TYPE),
                new FormalParameter<>("c", STRING_TYPE)
            ),
            true
        );

        assertEquals(
                "adminop op1(@node string a, string b, string c) string ",
                pmlStmtsOperationSignature.toFormattedString(0)
        );
    }

}