package gov.nist.csd.pm.core.pdp.modification;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventSubscriber;
import gov.nist.csd.pm.core.common.exception.PMException;

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
