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
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An interface for computing the privileges for an implied user. Unlike the AccessQuery interface which takes in the user
 * as input, this interface forces its implementations to provide the user. This is needed to support users being able to
 * "navigate" their own access state rather than another user's.
 */
public interface SelfAccessQuery {

    /**
     * Compute the privileges the user has on the target node.
     *
     * @param targetCtx The target node.
     * @return An AccessRightSet containing the user's privileges.
     * @throws PMException If there is an error in the PM.
     */
    AccessRightSet computePrivileges(TargetContext targetCtx) throws PMException;

    /**
     * Compute the privileges the user has on each target node.
     *
     * @param targetCtxs The target nodes.
     * @return A list of AccessRightSets containing the user's privileges for each target.
     * @throws PMException If there is an error in the PM.
     */
    List<AccessRightSet> computePrivileges(List<TargetContext> targetCtxs) throws PMException;

    /**
     * Compute the privileges that are denied for the user on the target node.
     *
     * @param targetCtx The target node.
     * @return An AccessRightSet containing the user's denied privileges.
     * @throws PMException If there is an error in the PM.
     */
    AccessRightSet computeDeniedPrivileges(TargetContext targetCtx) throws PMException;

    /**
     * Compute the privileges for all nodes in the subgraph starting at the root node.
     *
     * @param root The root node.
     * @return The SubgraphPrivileges for the root node.
     * @throws PMException If there is an error in the PM.
     */
    SubgraphPrivileges computeSubgraphPrivileges(long root) throws PMException;

    /**
     * Compute the privileges for the adjacent ascendants of the given root node.
     *
     * @param root The root node.
     * @return A mapping of the adjacent ascendants and the privileges on each.
     * @throws PMException If there is an error in the PM.
     */
    Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(long root) throws PMException;

    /**
     * Compute the privileges for the adjacent descendants of the given root node.
     *
     * @param root The root node.
     * @return A mapping of the adjacent descendants and the privileges on each.
     * @throws PMException If there is an error in the PM.
     */
    Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(long root) throws PMException;

    /**
     * Compute the original configuration of the user's Personal Object System.
     *
     * @return A map of nodes representing the first level of the user's POS and the privileges.
     * @throws PMException If there is an error in the PM.
     */
    Map<Node, AccessRightSet> computePersonalObjectSystem() throws PMException;

    Map<Long, Set<Long>> computeRequiredAttributeSets(TargetContext targetCtx, AccessRightSet privileges) throws PMException;

    Map<Long, AccessRightSet> computeCapabilityList() throws PMException;

}
