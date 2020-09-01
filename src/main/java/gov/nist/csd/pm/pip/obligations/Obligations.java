package gov.nist.csd.pm.pip.obligations;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.obligations.model.Obligation;

import java.util.List;

public interface Obligations {

    /**
     * Add the given obligation and enable it if the enable flag is true.
     * @param obligation the obligation to add
     * @param enable if true enable the obligation, else do not enable it
     * @throws PMException
     */
    void add(Obligation obligation, boolean enable) throws PMException;

    /**
     * Retrieves the obligation with the given label
     * @param label the label of the obligation to retrieve
     * @return the obligation with the given label
     * @throws PMException
     */
    Obligation get(String label) throws PMException;

    /**
     * Return all obligations
     * @return all obligations
     * @throws PMException
     */
    List<Obligation> getAll() throws PMException;

    /**
     * Update the obligation with the given label.  If the label in the provided object is not null and different from
     * the label parameter, the label will also be updated.
     * @param label the label of the obligation to update
     * @param obligation the updated obligation
     * @throws PMException
     */
    void update(String label, Obligation obligation) throws PMException;

    /**
     * Delete the obligation with the given label.
     * @param label the label of the obligation to delete
     * @throws PMException
     */
    void delete(String label) throws PMException;

    /**
     * Set the enable flag of the obligation with the given label.
     * @param label the label of the obligation to set the enable flag for
     * @param enabled the boolean flag to set for the given obligation
     * @throws PMException
     */
    void setEnable(String label, boolean enabled) throws PMException;

    /**
     * Returns all enabled obligations
     * @return all obligations that have a true enabled value
     * @throws PMException
     */
    List<Obligation> getEnabled() throws PMException;

}
