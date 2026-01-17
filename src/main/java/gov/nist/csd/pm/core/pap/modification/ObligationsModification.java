package gov.nist.csd.pm.core.pap.modification;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;

/**
 * NGAC obligation methods.
 */
public interface ObligationsModification {

    /**
     * Create a new obligation with the given author, name, event pattern and response. The author of the obligation is the user that the
     * responses will be executed as in the EPP. This means the author will need the privileges to carry out each action
     * in the response at the time it's executed. If they do not have sufficient privileges no action in the response
     * will be executed.
     *
     * @param authorId      The user/process that is creating the obligation.
     * @param name          The name of the obligation.
     * @param eventPattern  The obligation event pattern.
     * @param response      The obligation response.
     * @throws PMException  If any PM related exceptions occur in the implementing class.
     */
    void createObligation(long authorId, String name, EventPattern eventPattern, ObligationResponse response) throws PMException;

    /**
     * Delete the obligation with the given name. If the obligation does not exist, no exception is thrown as this is
     * the desired state.
     *
     * @param name The name of the obligation to delete.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    void deleteObligation(String name) throws PMException;

}
