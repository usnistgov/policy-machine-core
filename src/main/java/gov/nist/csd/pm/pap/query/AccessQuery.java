package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.query.explain.Explain;

import java.util.Collection;
import java.util.Map;

/**
 * Interface to query the access state of the policy.
 */
public interface AccessQuery {

    /**
     * Compute the privileges the user has on the target node.
     * @param userCtx The user and process (optional).
     * @param target The target node.
     * @return An AccessRightSet that contains the users privileges on the target node.
     * @throws PMException If there is an error in the PM.
     */
    AccessRightSet computePrivileges(UserContext userCtx, String target) throws PMException;

    /**
     * Compute the privileges that are denied for the user on the target node.
     * @param userCtx The user and process (optional).
     * @param target The target node.
     * @return An AccessRightSet that contains the users denied privileges on the target node.
     * @throws PMException If there is an error in the PM.
     */
    AccessRightSet computeDeniedPrivileges(UserContext userCtx, String target) throws PMException;

    /**
     * Compute the access rights that a user has access to under each policy class the target is an ascendant of. This
     * does not include prohibitions.
     * @param userCtx The user and process (optional).
     * @param target The target node.
     * @return A mapping of policy class names to the access rights the user has under them on the target node.
     * @throws PMException If there is an error in the PM.
     */
    Map<String, AccessRightSet> computePolicyClassAccessRights(UserContext userCtx, String target) throws PMException;

    /**
     * Compute a mapping of all the nodes the user has access to the access rights they have on each.
     * @param userCtx The user and process (optional).
     * @return A mapping of node names to access rights.
     * @throws PMException If there is an error in the PM.
     */
    Map<String, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException;

    /**
     * Compute the Access Control List for the given target.
     * @param target The target node.
     * @return A mapping of each user and their privileges on the target.
     * @throws PMException If there is an error in the PM.
     */
    Map<String, AccessRightSet> computeACL(String target) throws PMException;

    /**
     * Compute the attributes that are targets of associations in which the user attribute is a descendant of the user.
     * @param user The user node.
     * @return A mapping of the destination attributes to the access rights in the destination association.
     * @throws PMException If there is an error in the PM.
     */
    Map<String, AccessRightSet> computeDestinationAttributes(String user) throws PMException;

    /**
     * Compute the privileges for all ascendants of a given root node.
     * @param userCtx The user and process (optional).
     * @param root The root node.
     * @return A mapping of ascendant nodes to the privileges of the user.
     * @throws PMException If there is an error in the PM.
     */
    Map<String, AccessRightSet> computeAscendantPrivileges(UserContext userCtx, String root) throws PMException;

    /**
     * Explain why a user may or may not have privileges on a target node.
     * @param userCtx The user and process (optional).
     * @param target The target node.
     * @return An Explain object which enumerates the paths under each policy class that the user has access to the
     *         target node.
     * @throws PMException If there is an error in the PM.
     */
    Explain explain(UserContext userCtx, String target) throws PMException;

    /**
     * Compute the original configuration of a user's POS.
     *
     * @param userCtx The user and process (optional).
     * @return A set of nodes representing the first level of the user's POS.
     * @throws PMException If there is an error in the PM.
     */
    Collection<String> computePersonalObjectSystem(UserContext userCtx) throws PMException;

    /**
     * Compute the ascendants of the root node that the user has at least one access right on.
     * @param userCtx The user and process (optional).
     * @param root The root node.
     * @return The set of nodes that are ascendants of the root node and accessible by the user.
     * @throws PMException If there is an error in the PM.
     */
    Collection<String> computeAccessibleAscendants(UserContext userCtx, String root) throws PMException;

    /**
     * Compute the descendants of the root node that the user has at least one access right on.
     * @param userCtx The user and process (optional).
     * @param root The root node.
     * @return The set of nodes that are descendants of the root node and accessible by the user.
     * @throws PMException If there is an error in the PM.
     */
    Collection<String> computeAccessibleDescendants(UserContext userCtx, String root) throws PMException;

}
