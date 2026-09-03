/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.query;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
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
     * Get the node prohibitions for the given node.
     * @param nodeId the ID of the node.
     * @return the node prohibitions with the given node.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Prohibition> getNodeProhibitions(long nodeId) throws PMException;

    /**
     * Get the process prohibitions for the given process.
     * @param process the process.
     * @return the process prohibitions with the given process.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Prohibition> getProcessProhibitions(String process) throws PMException;

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
