package gov.nist.ngac.pm.core.pap.modification;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;

/**
 * NGAC obligation methods.
 */
public interface ObligationsModification {

    /**
     * Create the given obligation. The author of the obligation is the user that the responses will be executed as
     * in the EPP. This means the author will need the privileges to carry out each action in the response at the
     * time it's executed. If they do not have sufficient privileges no action in the response will be executed.
     *
     * @param obligation The obligation to create.
     * @throws PMException  If any PM related exceptions occur in the implementing class.
     */
    void createObligation(Obligation obligation) throws PMException;

    /**
     * Delete the obligation with the given name. If the obligation does not exist, no exception is thrown as this is
     * the desired state.
     *
     * @param name The name of the obligation to delete.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    void deleteObligation(String name) throws PMException;

}
