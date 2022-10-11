package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.events.PolicyEventListener;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.exceptions.PMRuntimeException;

class EventListener implements PolicyEventListener {

    private final EventProcessor eventProcessor;

    public EventListener(EventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    @Override
    public void handlePolicyEvent(PolicyEvent event) {
        if (!(event instanceof EventContext evtCtx)) {
            // ignore events that are not EventContext
            return;
        }

        try {
            this.eventProcessor.processEvent(evtCtx);
        } catch (PMException e) {
            throw new PMRuntimeException(e.getMessage());
        }
    }
}
