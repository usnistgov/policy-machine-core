package gov.nist.csd.pm.core.pdp;

import gov.nist.csd.pm.core.common.event.EventPublisher;
import gov.nist.csd.pm.core.common.event.EventSubscriber;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EventContext;
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
