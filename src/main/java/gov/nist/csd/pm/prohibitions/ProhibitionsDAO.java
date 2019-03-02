package gov.nist.csd.pm.prohibitions;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.prohibitions.model.Prohibition;

import java.util.List;

/**
 * Interface to maintain Prohibitions for an NGAC environment. This interface is in the common package because the
 * Prohibition service in the PDP will also implement this interface as well as any implementations in the PAP.
 */
public interface ProhibitionsDAO {
    /**
     * Create a new prohibition.
     *
     * @param prohibition The prohibition to be created.
     * @throws PMException if there is an error creating a prohibition.
     */
    void createProhibition(Prohibition prohibition) throws PMException;

    /**
     * Get a list of all prohibitions
     *
     * @return a list of all prohibitions
     * @throws PMException if there is an error getting the prohibitions.
     */
    List<Prohibition> getProhibitions() throws PMException;

    /**
     * Retrieve a Prohibition and return the Object representing it.
     *
     * @param prohibitionName The name of the Prohibition to retrieve.
     * @return the Prohibition with the given name.
     * @throws PMException if there is an error getting the prohibition with the given name.
     */
    Prohibition getProhibition(String prohibitionName) throws PMException;

    /**
     * Update the given prohibition.  The name of the prohibition is provided in the parameter.
     *
     * @param prohibition The prohibition to update.
     * @throws PMException if there is an error updating the prohibition.
     */
    void updateProhibition(Prohibition prohibition) throws PMException;

    /**
     * Delete the prohibition, and remove it from the data structure.
     *
     * @param prohibitionName The name of the prohibition to delete.
     * @throws PMException if there is an error deleting the prohibition.
     */
    void deleteProhibition(String prohibitionName) throws PMException;
}
