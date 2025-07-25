package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;

import java.util.Collection;

/**
 * Interface to query prohibitions.
 */
public interface ProhibitionsQuery {

    /**
     * Get all prohibitions, organized by the subject.
     *
     * @return All prohibitions.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Prohibition> getProhibitions() throws PMException;

    /**
     * Get prohibitions with the given subject.
     *
     * @param subject The subject to get the prohibitions for (user, user attribute, process)
     * @return The prohibitions with the given subject.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Prohibition> getProhibitionsWithSubject(ProhibitionSubject subject) throws PMException;

    /**
     * Get the prohibition with the given name.
     *
     * @param name The name of the prohibition to get.
     * @return The prohibition with the given name.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Prohibition getProhibition(String name) throws PMException;

    /**
     * Returns true if a prohibition with the provided name exists. Otherwise, false.
     *
     * @param name The name of the prohibition to check for.
     * @return True if a prohibition with the provided name exists. Otherwise, false.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    boolean prohibitionExists(String name) throws PMException;

    /**
     * Get the prohibitions the given subject inherits through assignments.
     *
     * @param subjectId The subject node.
     * @return The prohibitions the given subject inherits.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Prohibition> getInheritedProhibitionsFor(long subjectId) throws PMException;

    /**
     * Get the prohibitions that define the given container as a container condition.
     *
     * @param containerId The container to search for.
     * @return The prohibitions that define the given container as a container condition.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Prohibition> getProhibitionsWithContainer(long containerId) throws PMException;

}
