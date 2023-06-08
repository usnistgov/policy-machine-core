package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.EventContext;

/**
 * Interface for processing EventContexts in the EPP.
 */
public interface EventProcessor {

    /**
     * Process the given EventContext in the EPP.
     * @param eventCtx The event context to process.
     * @throws PMException If there is an error processing the EventContext.
     */
    void processEvent(EventContext eventCtx) throws PMException;

}
