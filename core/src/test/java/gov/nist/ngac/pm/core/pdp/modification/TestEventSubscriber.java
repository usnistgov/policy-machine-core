package gov.nist.ngac.pm.core.pdp.modification;

import gov.nist.ngac.pm.core.common.event.EventSubscriber;
import gov.nist.ngac.pm.core.epp.EventContext;

public class TestEventSubscriber implements EventSubscriber {

    private EventContext eventContext;

    public EventContext getEventContext() {
        return eventContext;
    }

    @Override
    public void processEvent(EventContext eventCtx) {
        this.eventContext = eventCtx;
    }
}
