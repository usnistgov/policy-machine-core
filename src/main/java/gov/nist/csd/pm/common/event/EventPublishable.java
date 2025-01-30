package gov.nist.csd.pm.common.event;

import gov.nist.csd.pm.common.event.operand.OperandValue;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.Map;

public interface EventPublishable {

	default EventContext toEventContext(PAP pap, UserContext userCtx, Operation<?> operation, Map<String, OperandValue> operands) throws PMException {
		return new EventContext(
				pap.query().graph().getNodeById(userCtx.getUser()).getName(),
				userCtx.getProcess(),
				operation,
				operands
		);
	}

}
