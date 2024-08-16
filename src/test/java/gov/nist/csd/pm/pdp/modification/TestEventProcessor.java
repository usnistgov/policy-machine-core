package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.obligation.EventContext;
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
