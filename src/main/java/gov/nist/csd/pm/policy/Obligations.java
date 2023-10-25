package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;

import java.util.List;

/**
 * NGAC obligation methods.
 */
public interface Obligations {

    /**
     * Create a new obligation with the given author, name, and rules.
     *
     * @param author The user/process that is creating the obligation.
     * @param name The name of the obligation.
     * @param rules The rules of the obligation.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    void create(UserContext author, String name, Rule... rules) throws PMException;

    /**
     * Update the author and rules of the obligation with the given name.
     *
     * @param author The user/process that updated the obligation.
     * @param name The name of the obligation to update.
     * @param rules The updated obligation rules.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    void update(UserContext author, String name, Rule... rules) throws PMException;

    /**
     * Delete the obligation with the given name.
     *
     * @param name The name of the obligation to delete.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    void delete(String name) throws PMException;

    /**
     * Get all obligations.
     *
     * @return All obligations.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    List<Obligation> getAll() throws PMException;

    /**
     * Check if an obligation exists with the given name.
     *
     * @param name The obligation to check.
     * @return True if the obligation exists with the given name, false otherwise.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    boolean exists(String name) throws PMException;

    /**
     * Get the obligation associated with the given name.
     *
     * @param name The name of the obligation to get.
     * @return The obligation object associated with the given name.
     * @throws PMException If there is an error during the execution process at the implementation level.
     */
    Obligation get(String name) throws PMException;

}
