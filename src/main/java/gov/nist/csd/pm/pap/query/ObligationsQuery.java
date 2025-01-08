package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.Obligation;

import java.util.Collection;

/**
 * Interface to query obligations.
 */
public interface ObligationsQuery {

    /**
     * Get all obligations.
     *
     * @return All obligations.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Obligation> getObligations() throws PMException;

    /**
     * Check if an obligation exists with the given name.
     *
     * @param name The obligation to check.
     * @return True if the obligation exists with the given name, false otherwise.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    boolean obligationExists(String name) throws PMException;

    /**
     * Get the obligation associated with the given name.
     *
     * @param name The name of the obligation to get.
     * @return The obligation object associated with the given name.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Obligation getObligation(String name) throws PMException;

    /**
     * Get the obligations created by the given author.
     *
     * @param user The user context representing the author to search for.
     * @return A list of Obligation objects.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Obligation> getObligationsWithAuthor(String user) throws PMException;
    
}
