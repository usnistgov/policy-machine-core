package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.pap.pml.statement.ForeachStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.statement.operation.CheckStatement;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PMLStmtsOperationSignatureTest {

    @Test
    void testToFormattedString() {
        PMLStmtsOperationSignature pmlStmtsOperationSignature = new PMLStmtsOperationSignature(
                "op1",
                Type.string(),
                List.of("a", "b", "c"),
                List.of("a"),
                Map.of("a", Type.array(Type.string()), "b", Type.string(), "c", Type.string()),
                new PMLStatementBlock()
        );

        assertEquals(
                "operation op1(nodeop []string a, string b, string c) string ",
                pmlStmtsOperationSignature.toFormattedString(0)
        );


        /*pmlStmtsOperationSignature = new PMLStmtsOperationSignature(
                pmlStmtsOperationSignature.getFunctionName(),
                pmlStmtsOperationSignature.getReturnType(),
                pmlStmtsOperationSignature.getOperands(),
                pmlStmtsOperationSignature.getNodeOperands(),
                pmlStmtsOperationSignature.getOperandTypes(),
                new PMLStatementBlock(
                        new ForeachStatement("varname", "", List.of(
                                new CheckStatement()
                        ))
                )
        );*/
    }

}