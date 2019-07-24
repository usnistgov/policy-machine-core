package gov.nist.csd.pm.pdp.decider;

import gov.nist.csd.pm.exceptions.PMException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Interface for making access decisions.  These methods do not take into account prohibited permissions.
 */
public interface Decider {

    /**
     * Check if the user has the permissions on the target node. Use '*' as the permission to check
     * if the user has any permissions on the node.
     *
     * @param userID    the ID of the user.
     * @param processID    the ID of the process if applicable.
     * @param targetID  the ID of the target node.
     * @param perms     the array of permission sto check for.
     * @return true if the user has the permissions on the target node, false otherwise.
     * @throws PMException if there is an exception traversing the graph.
     */
    boolean check(long userID, long processID, long targetID, String... perms) throws PMException;

    /**
     * List the permissions that the user has on the target node.
     *
     * @param userID    the ID of the user.
     * @param processID    the ID of the process if applicable.
     * @param targetID  the ID of the target node.
     * @return the set of operations that the user is allowed to perform on the target.
     * @throws PMException if there is an exception traversing the graph.
     */
    Set<String> list(long userID, long processID, long targetID) throws PMException;

    /**
     * Given a list of nodes filter out any nodes that the given user does not have the given permissions on. To filter
     * based on any permissions use Operations.ANY as the permission to check for.
     *
     * @param userID    the ID of the user.
     * @param processID    the ID of the process if applicable.
     * @param nodes     the nodes to filter from.
     * @param perms     the permissions to check for.
     * @return a subset of the given nodes that the user has the given permissions on.
     */
    Collection<Long> filter(long userID, long processID, Collection<Long> nodes, String... perms) throws PMException;

    /**
     * Get the children of the target node that the user has the given permissions on.
     *
     * @param userID    the ID of the user.
     * @param processID    the ID of the process if applicable.
     * @param targetID  the ID of the target node.
     * @param perms     the permissions the user must have on the child nodes.
     * @return the set of NGACNodes that are children of the target node and the user has the given permissions on.
     * @throws PMException if there is an exception traversing the graph.
     */
    Collection<Long> getChildren(long userID, long processID, long targetID, String... perms) throws PMException;

    /**
     * Given a User ID, return every node the user has access to and the permissions they have on each.
     *
     * @param userID the ID of the User.
     * @param processID    the ID of the process if applicable.
     * @return a map of nodes the user has access to and the permissions on each.
     * @throws PMException if there is an error traversing the graph.
     */
    Map<Long, Set<String>> getAccessibleNodes(long userID, long processID) throws PMException;
}


