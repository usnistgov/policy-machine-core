package gov.nist.ngac.pm.core.pdp;

import gov.nist.ngac.pm.core.common.event.EventPublisher;
import gov.nist.ngac.pm.core.common.event.EventSubscriber;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.epp.EventContext;
import java.util.List;

/**
 * {@link EventPublisher} that forwards published events to each registered subscriber in order.
 */
public class PDPEventPublisher implements EventPublisher {

    private final List<EventSubscriber> epps;

    public PDPEventPublisher(List<EventSubscriber> epps) {
        this.epps = epps;
    }

    @Override
    public void addEventSubscriber(EventSubscriber processor) {
        this.epps.add(processor);
    }

    @Override
    public void removeEventSubscriber(EventSubscriber processor) {
        this.epps.remove(processor);
    }

    @Override
    public void publishEvent(EventContext event) throws PMException {
        for (EventSubscriber epp : epps) {
            epp.processEvent(event);
        }
    }
}
