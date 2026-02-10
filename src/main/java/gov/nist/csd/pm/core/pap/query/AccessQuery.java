package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.explain.Explain;
import gov.nist.csd.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import java.util.List;
import java.util.Map;

/**
 * Interface to query the access state of the policy.
 */
public interface AccessQuery {

    /**
     * Compute the privileges the user has on the target node. The provided User and Target contexts, allow for the
     * specification of a single node or a list of attributes.
     *
     * @param userCtx   The user and process or list of attributes and process. Process is optional.
     * @param targetCtx The target node or list of attributes.
     * @return An AccessRightSet that contains the users privileges on the target node.
     * @throws PMException If there is an error in the PM.
     */
    AccessRightSet computePrivileges(UserContext userCtx, TargetContext targetCtx) throws PMException;

    /**
     * Compute the privileges the user has on each target node. The provided User and Target contexts, allow for the
     * specification of a single node or a list of attributes.
     *
     * @param userCtx   The user and process or list of attributes and process. Process is optional.
     * @param targetCtxs The target nodes.
     * @return An AccessRightSet that contains the users privileges on the target node.
     * @throws PMException If there is an error in the PM.
     */
    List<AccessRightSet> computePrivileges(UserContext userCtx, List<TargetContext> targetCtxs) throws PMException;

    /**
     * Compute the privileges that are denied for the user on the target node.The provided User and Target contexts,
     * allow for the specification of a single node or a list of attributes.
     * @param userCtx   The user and process or list of attributes and process. Process is optional.
     * @param targetCtx The target node or list of attributes.
     * @return An AccessRightSet that contains the users denied privileges on the target node.
     * @throws PMException If there is an error in the PM.
     */
    AccessRightSet computeDeniedPrivileges(UserContext userCtx, TargetContext targetCtx) throws PMException;

    /**
     * Compute a mapping of all the nodes the user has access to the access rights they have on each. The provided
     * UserContext allows for the specification of a single node or a list of attributes.
     *
     * @param userCtx The user and process or list of attributes and process. Process is optional.
     * @return A mapping of node names to access rights.
     * @throws PMException If there is an error in the PM.
     */
    Map<Long, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException;

    /**
     * Compute the Access Control List for the node or a list of attributes. The provided TargetContext allows for the
     * specification of a single node or a list of attributes.
     *
     * @param targetCtx The target node or list of attributes.
     * @return A mapping of each user and their privileges on the target.
     * @throws PMException If there is an error in the PM.
     */
    Map<Long, AccessRightSet> computeACL(TargetContext targetCtx) throws PMException;

    /**
     * Compute the attributes that are targets of associations in which the user attribute is a descendant of the user.
     * The provided UserContext allows for the specification of a single node or a list of attributes.
     *
     * @param userCtx The user or list of attributes. Process is ignored.
     * @return A mapping of the destination attributes to the access rights in the destination association.
     * @throws PMException If there is an error in the PM.
     */
    Map<Long, AccessRightSet> computeDestinationAttributes(UserContext userCtx) throws PMException;

    /**
     * Compute the privileges for all nodes in the subgraph starting at the root node. The returned Subgraph object stores
     * the privileges for the user on the root node and a recursive list of the users access to the root node's subgraph.
     * Any node that the user does not have access to will be included in the result but will have an empty privileges set.
     * The provided UserContext allows for the specification of a single node or a list of attributes.
     *
     * @param userCtx   The user and process or list of attributes and process. Process is optional.
     * @param root The root node.
     * @return The Subgraph for the root node.
     * @throws PMException If there is an error in the PM.
     */
    SubgraphPrivileges computeSubgraphPrivileges(UserContext userCtx, long root) throws PMException;

    /**
     * Compute the privileges for the adjacent ascendants of the given root node. Any node that the user does not have
     * access to will be included in the result but will have an empty privileges set. The provided UserContext allows
     * for the specification of a single node or a list of attributes.
     *
     * @param userCtx   The user and process or list of attributes and process. Process is optional.
     * @param root The root node.
     * @return A Map of the adjacent ascendants of the root node the user has access to and the privileges on each.
     * @throws PMException If there is an error in the PM.
     */
    Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(UserContext userCtx, long root) throws PMException;

    /**
     * Compute the privileges for the adjacent descendants of the given root node. Any node that the user does not have
     * access to will be included in the result but will have an empty privileges set. The provided UserContext allows
     * for the specification of a single node or a list of attributes.
     *
     * @param userCtx The user and process or list of attributes and process. Process is optional.
     * @param root    The root node.
     * @return A Map of the adjacent descendants of the root node the user has access to and the privileges on each.
     * @throws PMException If there is an error in the PM.
     */
    Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(UserContext userCtx, long root) throws PMException;

    /**
     * Explain why a user may or may not have privileges on a target node. The provided User and Target contexts, allow
     * for the specification of a single node or a list of attributes.
     *
     * @param userCtx   The user and process or list of attributes and process. Process is optional.
     * @param targetCtx The target node or list of attributes.
     * @return An Explain object which enumerates the paths under each policy class that the user has access to the
     *         target node.
     * @throws PMException If there is an error in the PM.
     */
    Explain explain(UserContext userCtx, TargetContext targetCtx) throws PMException;

    /**
     * Compute the original configuration of a user's Personal Object System. The returned nodes are the nodes closest
     * to policy class nodes that the user has privileges on. If the user has privileges on policy classes, the returned
     * nodes will just be the set of policy classes. The provided UserContext allows for the specification of a single
     * node or a list of attributes.
     *
     * @param userCtx The user and process or list of attributes and process. Process is optional.
     * @return A map of nodes representing the first level of the user's POS and the privileges of the user.
     * @throws PMException If there is an error in the PM.
     */
    Map<Node, AccessRightSet> computePersonalObjectSystem(UserContext userCtx) throws PMException;

}
