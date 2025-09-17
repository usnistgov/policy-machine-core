package gov.nist.csd.pm.core.pap.obligation;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;

import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.io.Serializable;

public interface ObligationResponse extends Serializable {

    void execute(PAP pap, UserContext author, EventContext evtCtx) throws PMException;
}