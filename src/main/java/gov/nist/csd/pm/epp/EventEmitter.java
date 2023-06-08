package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.EventContext;

/**
 * Interface for emitting events to be processed by an EPP.
 */
public interface EventEmitter {

    /**
     * Add the given EventProcessor as a listener for policy events.
     * @param processor The processor to add as a listener.
     */
    void addEventListener(EventProcessor processor);

    /**
     * Remove the given EventProcessor as a listener.
     * @param processor The processor to remove.
     */
    void removeEventListener(EventProcessor processor);

    /**
     * Emit the given event context to any EventProcessors listening.
     * @param event The EventContext to emit.
     * @throws PMException If there is an exception emitting the event.
     */
    void emitEvent(EventContext event) throws PMException;

}
