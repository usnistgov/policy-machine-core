package gov.nist.csd.pm.decider;

import gov.nist.csd.pm.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMGraphException;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;

import java.util.HashSet;

/**
 * Interface for making access decisions.  These methods do not take into account prohibited permissions.
 */
public interface Decider {
    /**
     * Check if the user has the permissions on the target node. Use '*' as the permission to check
     * if the user has any permissions on the node.
     *
     * @param userID the ID of the user.
     * @param processID the of the process.
     * @param targetID the ID of the target node.
     * @param perms the array of permission sto check for.
     * @return true if the user has the permissions on the target node, false otherwise.
     * @throws PMDBException if there is an error accessing the graph in a database.
     * @throws PMGraphException if there is an exception traversing the graph.
     */
    boolean hasPermissions(long userID, long processID, long targetID, String... perms) throws PMGraphException, PMDBException;

    /**
     * List the permissions that the user has on the target node.
     *
     * @param userID the ID of the user.
     * @param processID the of the process.
     * @param targetID the ID of the target node.
     * @return the set of operations that the user is allowed to perform on the target.
     * @throws PMDBException if there is an error accessing the graph in a database.
     * @throws PMGraphException if there is an exception traversing the graph.
     */
    HashSet<String> listPermissions(long userID, long processID, long targetID) throws PMDBException, PMGraphException;

    /**
     * Given a list of nodes filter out any nodes that the given user does not have the given permissions on. To filter
     * based on any permissions use Operations.ANY as the permission to check for.
     * @param userID the ID of the user.
     * @param processID the of the process.
     * @param nodes the nodes to filter from.
     * @param perms the permissions to check for.
     * @return a subset of the given nodes that the user has the given permissions on.
     */
    HashSet<NodeContext> filter(long userID, long processID, HashSet<NodeContext> nodes, String... perms);

    /**
     * Get the children of the target node that the user has the given permissions on.
     *
     * @param userID the ID of the user.
     * @param processID the of the process.
     * @param targetID the ID of the target node.
     * @param perms the permissions the user must have on the child nodes.
     * @return the set of NGACNodes that are children of the target node and the user has the given permissions on.
     * @throws PMDBException if there is an error accessing the graph in a database.
     * @throws PMGraphException if there is an exception traversing the graph.
     */
    HashSet<NodeContext> getChildren(long userID, long processID, long targetID, String... perms) throws PMGraphException, PMDBException;
}
