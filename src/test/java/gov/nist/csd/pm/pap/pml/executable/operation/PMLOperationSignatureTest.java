package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.pap.executable.arg.FormalArg;
import gov.nist.csd.pm.pap.executable.op.arg.NodeFormalArg;
import gov.nist.csd.pm.pap.pml.executable.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.PAPTest.ARG_A;
import static gov.nist.csd.pm.pap.PAPTest.ARG_B;
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