package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.prohibition.Prohibition;

import java.util.Collection;
import java.util.Map;

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
    Map<Node, Collection<Prohibition>> getProhibitions() throws PMException;

    /**
     * Get prohibitions with the given subject.
     *
     * @param subject The subject to get the prohibitions for (user, user attribute, process)
     * @return The prohibitions with the given subject.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Prohibition> getProhibitionsWithSubject(String subject) throws PMException;

    /**
     * Get the prohibition with the given name.
     * @param name The public abstract of the prohibition to get.
     * @return The prohibition with the given public abstract.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Prohibition getProhibition(String name) throws PMException;

    /**
     * Get the prohibitions the given subject inherits through assignments.
     * @param subject The subject node.
     * @return The prohibitions the given subject inherits.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Prohibition> getInheritedProhibitionsFor(String subject) throws PMException;

    /**
     * Get the prohibitions that define the given container as a container condition.
     * @param container The container to search for.
     * @return The prohibitions that define the given container as a container condition.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Prohibition> getProhibitionsWithContainer(String container) throws PMException;

}
