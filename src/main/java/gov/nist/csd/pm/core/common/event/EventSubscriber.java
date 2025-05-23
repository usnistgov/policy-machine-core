package gov.nist.csd.pm.core.common.event;

import gov.nist.csd.pm.core.common.exception.PMException;

/**
 * Interface for processing EventContexts.
 */
public interface EventSubscriber {

    /**
     * Subscribes to the given eventPublisher.
     * @param eventPublisher The EventPublisher to subscribe to.
     */
    default void subscribeTo(EventPublisher eventPublisher) {
        eventPublisher.addEventSubscriber(this);
    }

    /**
     * Process the given EventContext.
     * @param eventCtx The event context to process.
     * @throws PMException If there is an error processing the EventContext.
     */
    void processEvent(EventContext eventCtx) throws PMException;

}
