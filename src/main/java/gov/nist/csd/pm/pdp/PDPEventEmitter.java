package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.EventContext;
import gov.nist.csd.pm.epp.EventEmitter;
import gov.nist.csd.pm.epp.EventProcessor;

import java.util.List;

public class PDPEventEmitter implements EventEmitter {

    private List<EventProcessor> epps;

    public PDPEventEmitter(List<EventProcessor> epps) {
        this.epps = epps;
    }

    @Override
    public void addEventListener(EventProcessor processor) {
        this.epps.add(processor);
    }

    @Override
    public void removeEventListener(EventProcessor processor) {
        this.epps.remove(processor);
    }

    @Override
    public void emitEvent(EventContext event) throws PMException {
        for (EventProcessor epp : epps) {
            epp.processEvent(event);
        }
    }
}
