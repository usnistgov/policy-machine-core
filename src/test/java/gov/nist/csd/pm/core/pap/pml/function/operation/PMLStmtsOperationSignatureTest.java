package gov.nist.csd.pm.core.pap.pml.function.operation;


import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeFormalParameter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.*;

class PMLStmtsOperationSignatureTest {

    @Test
    void testToFormattedString() {
        PMLOperationSignature pmlStmtsOperationSignature = new PMLOperationSignature(
                "op1",
                STRING_TYPE,
            List.of(
                new NodeFormalParameter("a"),
                new FormalParameter<>("b", STRING_TYPE),
                new FormalParameter<>("c", STRING_TYPE)
            )
        );

        assertEquals(
                "operation op1(@node string a, string b, string c) string ",
                pmlStmtsOperationSignature.toFormattedString(0)
        );
    }

}