package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.event.EventPublisher;
import gov.nist.csd.pm.common.event.EventSubscriber;

import java.util.List;

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
