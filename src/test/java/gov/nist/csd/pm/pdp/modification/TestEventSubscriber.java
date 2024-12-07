package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.event.EventSubscriber;

public class TestEventSubscriber implements EventSubscriber {

    private EventContext eventContext;

    public EventContext getEventContext() {
        return eventContext;
    }

    @Override
    public void processEvent(EventContext eventCtx) throws PMException {
        this.eventContext = eventCtx;
    }
}
