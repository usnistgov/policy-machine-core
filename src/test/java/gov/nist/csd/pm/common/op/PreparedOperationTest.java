package gov.nist.csd.pm.common.op;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.common.op.graph.GraphOp.ASCENDANT_OPERAND;
import static gov.nist.csd.pm.common.op.graph.GraphOp.DESCENDANTS_OPERAND;
import static org.junit.jupiter.api.Assertions.*;

class PreparedOperationTest {

    @Test
    void testExecute() throws PMException {
        Operation<Void> op1 = new Operation<>("op1", List.of("a", "b", "c", "d"), List.of("c", "d")) {
            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

            }

            @Override
            public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
                pap.modify().graph().createPolicyClass("ok");

                return null;
            }
        };

        PreparedOperation<Void> preparedOperation = new PreparedOperation<>(
                op1, Map.of(
                ASCENDANT_OPERAND, "c",
                DESCENDANTS_OPERAND, List.of("a", "b")
        )
        );

        EventContext execute = preparedOperation.execute(new MemoryPAP(), new UserContext("u1"), new PrivilegeChecker(new MemoryPAP()));
        assertEquals(execute, new EventContext("u1", null, op1,
                Map.of(ASCENDANT_OPERAND, "c", DESCENDANTS_OPERAND, List.of("a", "b"))));
    }

}