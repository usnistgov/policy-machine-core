package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;

import java.util.List;

public interface Obligations {

    /**
     * Create a new obligation.
     *
     * @param author the user/process that is creating the obligation.
     * @param label the label of the obligation.
     * @param rules the rules of the obligation.
     * @throws PMException
     */
    void create(UserContext author, String label, Rule... rules) throws PMException;

    /**
     * Update the obligation with the given label.  If the label in the provided object is not null and different from
     * the label parameter, the label will also be updated.
     * @param author the user/process that created the obligation
     * @param label the label of the obligation to update
     * @param rules the updated obligation rules
     * @throws PMException
     */
    void update(UserContext author, String label, Rule... rules) throws PMException;

    /**
     * Delete the obligation with the given label.
     * @param label the label of the obligation to delete
     * @throws PMException
     */
    void delete(String label) throws PMException;

    /**
     * Get all obligations.
     * @return All obligations.
     * @throws PMException
     */
    List<Obligation> getAll() throws PMException;

    /**
     * Check if an obligation exists with the given label.
     * @param label The obligation to check.
     * @return True if the obligation exists with the given label, false otherwise.
     * @throws PMException
     */
    boolean exists(String label) throws PMException;

    /**
     * Get the Obligation object associated with the given label.
     * @param label The label of the obligation to get.
     * @return The Obligation object associated with the given label.
     * @throws PMException
     */
    Obligation get(String label) throws PMException;

}
