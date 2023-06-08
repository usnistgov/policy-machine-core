package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.EventContext;
import gov.nist.csd.pm.epp.EventProcessor;

public class TestEventProcessor implements EventProcessor {

    private EventContext eventContext;

    public EventContext getEventContext() {
        return eventContext;
    }

    @Override
    public void processEvent(EventContext eventCtx) throws PMException {
        this.eventContext = eventCtx;
    }
}
