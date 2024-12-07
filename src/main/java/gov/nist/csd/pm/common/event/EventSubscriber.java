package gov.nist.csd.pm.common.event;

import gov.nist.csd.pm.common.exception.PMException;

/**
 * Interface for processing EventContexts.
 */
public interface EventSubscriber {

    /**
     * Process the given EventContext.
     * @param eventCtx The event context to process.
     * @throws PMException If there is an error processing the EventContext.
     */
    void processEvent(EventContext eventCtx) throws PMException;

}
