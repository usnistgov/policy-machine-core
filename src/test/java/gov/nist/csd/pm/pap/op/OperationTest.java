package gov.nist.csd.pm.pap.op;

import gov.nist.csd.pm.common.exception.OperandsDoNotMatchException;
import gov.nist.csd.pm.pap.op.graph.AssignOp;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.graph.GraphOp.ASCENDANT_OPERAND;
import static gov.nist.csd.pm.pap.op.graph.GraphOp.DESCENDANTS_OPERAND;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OperationTest {

    @Test
    void testValidateOperands() {
        AssignOp assignOp = new AssignOp();
        assertThrows(OperandsDoNotMatchException.class,
                () -> assignOp.validateOperands(Map.of("a", "a", "b", "b", "c", "c")));
        assertThrows(OperandsDoNotMatchException.class,
                () -> assignOp.validateOperands(Map.of(ASCENDANT_OPERAND, "a",
                        DESCENDANTS_OPERAND, List.of("b"), "c", "c")));

    }

    @Test
    void testIllegalOperandValueException() throws OperandsDoNotMatchException {
        AssignOp assignOp = new AssignOp();
        assertDoesNotThrow(() -> assignOp.withOperands(Map.of(ASCENDANT_OPERAND, "a", DESCENDANTS_OPERAND, List.of("b"))));

        assertThrows(IllegalArgumentException.class, () -> assignOp.withOperands(Map.of(ASCENDANT_OPERAND, "a", DESCENDANTS_OPERAND, Map.of("a", "b"))));
    }
}