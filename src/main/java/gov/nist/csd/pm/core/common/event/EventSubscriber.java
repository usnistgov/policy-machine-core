package gov.nist.csd.pm.core.common.event;

import gov.nist.csd.pm.core.epp.EventContext;

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
     * Process the given EventContext. This method should not throw an exception as EPP errors should not
     * affect the adjudication of the operation that triggered them.
     * @param eventCtx The event context to process.
     */
    void processEvent(EventContext eventCtx);

}
