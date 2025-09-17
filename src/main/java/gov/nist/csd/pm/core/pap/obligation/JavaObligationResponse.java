package gov.nist.csd.pm.core.pap.obligation;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

public final class JavaObligationResponse implements ObligationResponse {

    private final ObligationResponseExecutor executor;

    public JavaObligationResponse(ObligationResponseExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(PAP pap, UserContext author, EventContext evtCtx) throws PMException {
        this.executor.execute(pap, author, evtCtx);
    }

    public interface ObligationResponseExecutor {
        void execute(PAP pap, UserContext author, EventContext evtCtx) throws PMException;
    }
}
