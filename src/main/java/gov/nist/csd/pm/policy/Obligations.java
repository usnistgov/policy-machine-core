package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;

import java.util.List;

/**
 * NGAC obligation methods.
 */
public interface Obligations {

    /**
     * Create a new obligation with the given author, id, and rules. The author of the obligation is the user that the
     * responses will be executed as in the EPP. This means the author will need the privileges to carry out each action
     * in the response at the time it's executed. If they do not have sufficient privileges no action in the response
     * will be executed.
     *
     * @param author The user/process that is creating the obligation.
     * @param id The id of the obligation.
     * @param rules The rules of the obligation.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    void create(UserContext author, String id, Rule... rules) throws PMException;

    /**
     * Update the obligation with the given id. This will overwrite any existing rules to the rules provided.
     *
     * @param author The user/process that created the obligation.
     * @param id The id of the obligation to update.
     * @param rules The updated obligation rules.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    void update(UserContext author, String id, Rule... rules) throws PMException;

    /**
     * Delete the obligation with the given id. If the obligation exists, no exception is thrown as this is
     * the desired state.
     *
     * @param id The id of the obligation to delete.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    void delete(String id) throws PMException;

    /**
     * Get all obligations.
     *
     * @return All obligations.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    List<Obligation> getAll() throws PMException;

    /**
     * Check if an obligation exists with the given id.
     *
     * @param id The obligation to check.
     * @return True if the obligation exists with the given id, false otherwise.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    boolean exists(String id) throws PMException;

    /**
     * Get the obligation associated with the given id.
     *
     * @param id The id of the obligation to get.
     * @return The obligation object associated with the given id.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    Obligation get(String id) throws PMException;

}
