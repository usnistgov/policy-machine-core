package gov.nist.csd.pm.core.common.event;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EventContext;

/**
 * Interface for emitting events to be processed by an EPP.
 */
public interface EventPublisher {

    /**
     * Add the given EventProcessor as a listener for policy events.
     * @param processor The processor to add as a listener.
     */
    void addEventSubscriber(EventSubscriber processor);

    /**
     * Remove the given EventProcessor as a listener.
     * @param processor The processor to remove.
     */
    void removeEventSubscriber(EventSubscriber processor);

    /**
     * Emit the given event context to any EventProcessors listening.
     * @param event The EventContext to emit.
     * @throws PMException If there is an exception emitting the event.
     */
    void publishEvent(EventContext event) throws PMException;

}
