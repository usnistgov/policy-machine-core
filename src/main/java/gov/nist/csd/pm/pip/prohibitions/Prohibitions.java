package gov.nist.csd.pm.pip.prohibitions;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.List;

/**
 * Interface to maintain Prohibitions for an NGAC environment. This interface is in the common package because the
 * Prohibition service in the PDP will also implement this interface as well as any implementations in the PAP.
 */
public interface Prohibitions {

    /**
     * Create a new prohibition.
     *
     * @param prohibition The prohibition to be created.
     * @throws PMException if there is an error creating a prohibition.
     */
    void add(Prohibition prohibition) throws PMException;

    /**
     * Get a list of all prohibitions
     *
     * @return a list of all prohibitions
     * @throws PMException if there is an error getting the prohibitions.
     */
    List<Prohibition> getAll() throws PMException;

    /**
     * Retrieve a Prohibition and return the Object representing it.
     *
     * @param prohibitionName The name of the Prohibition to retrieve.
     * @return the Prohibition with the given name.
     * @throws PMException if there is an error getting the prohibition with the given name.
     */
    Prohibition get(String prohibitionName) throws PMException;

    /**
     * Get all of the prohibitions a given entity is the direct subject of.  The subject can be a user, user attribute,
     * or process.
     * @param subject the name of the subject to get the prohibitions for.
     * @return The list of prohibitions the given entity is the subject of.
     */
    List<Prohibition> getProhibitionsFor(String subject) throws PMException;

    /**
     * Update the prohibition with the given name. Prohibition names cannot be updated.
     *
     * @param prohibitionName the name of the prohibition to update.
     * @param prohibition The prohibition to update.
     * @throws PMException if there is an error updating the prohibition.
     */
    void update(String prohibitionName, Prohibition prohibition) throws PMException;

    /**
     * Delete the prohibition, and remove it from the data structure.
     *
     * @param prohibitionName The name of the prohibition to delete.
     * @throws PMException if there is an error deleting the prohibition.
     */
    void delete(String prohibitionName) throws PMException;
}
