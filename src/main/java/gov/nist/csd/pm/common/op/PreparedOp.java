package gov.nist.csd.pm.common.op;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

public abstract class PreparedOp {

	public abstract EventContext getEventContext(UserContext userCtx, PAP pap) throws PMException;

}
