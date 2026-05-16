package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
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

}
