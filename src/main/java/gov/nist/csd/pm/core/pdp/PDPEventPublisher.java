package gov.nist.csd.pm.core.pdp;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventPublisher;
import gov.nist.csd.pm.core.common.event.EventSubscriber;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PDPEventPublisher implements EventPublisher {

    private final List<EventSubscriber> epps;

    public PDPEventPublisher() {
        this.epps = new ArrayList<>();
    }

    public PDPEventPublisher(List<EventSubscriber> epps) {
        this.epps = epps;
    }

    public List<EventSubscriber> getEpps() {
        return epps;
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

    public void publishOperationDeniedEvent(PAP pap, UserContext userCtx, String deniedOpName, Args args) throws PMException {
        EventContext eventCtx = new EventContext(
            pap, userCtx, "op_denied", Map.of(
            "op_name", deniedOpName,
            "timestamp", System.nanoTime(),
            "args", args.toMap()
        ));

        publishEvent(eventCtx);
    }
}
