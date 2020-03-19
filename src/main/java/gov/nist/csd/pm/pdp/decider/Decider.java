package gov.nist.csd.pm.pdp.decider;

import gov.nist.csd.pm.exceptions.PMException;

import java.util.Map;
import java.util.Set;

/**
 * Interface for making access decisions.
 */
public interface Decider {

    /**
     * Check if the subject has the permissions on the target node. Use '*' as the permission to check
     * if the subject has any permissions on the node.
     *
     * @param subject    the name of the subject.
     * @param process    the name of the process if applicable.
     * @param target  the name of the target node.
     * @param perms     the array of permission sto check for.
     * @return true if the subject has the permissions on the target node, false otherwise.
     * @throws PMException if there is an exception traversing the graph.
     */
    boolean check(String subject, String process, String target, String... perms) throws PMException;

    /**
     * List the permissions that the subject has on the target node.
     *
     * @param subject    the name of the subject.
     * @param process    the name of the process if applicable.
     * @param target  the name of the target node.
     * @return the set of operations that the subject is allowed to perform on the target.
     * @throws PMException if there is an exception traversing the graph.
     */
    Set<String> list(String subject, String process, String target) throws PMException;

    /**
     * Given a list of nodes filter out any nodes that the given subject does not have the given permissions on. To filter
     * based on any permissions use Operations.ANY as the permission to check for.
     *
     * @param subject    the name of the subject.
     * @param process    the name of the process if applicable.
     * @param nodes     the nodes to filter from.
     * @param perms     the permissions to check for.
     * @return a subset of the given nodes that the subject has the given permissions on.
     */
    Set<String> filter(String subject, String process, Set<String> nodes, String... perms) throws PMException;

    /**
     * Get the children of the target node that the subject has the given permissions on.
     *
     * @param subject    the name of the subject.
     * @param process    the name of the process if applicable.
     * @param target  the name of the target node.
     * @param perms     the permissions the subject must have on the child nodes.
     * @return the set of NGACNodes that are children of the target node and the subject has the given permissions on.
     * @throws PMException if there is an exception traversing the graph.
     */
    Set<String> getChildren(String subject, String process, String target, String... perms) throws PMException;

    /**
     * Given a subject, return every node the subject has access to and the permissions they have on each.
     *
     * @param subject the name of the subject.
     * @param process    the name of the process if applicable.
     * @return a map of nodes the subject has access to and the permissions on each.
     * @throws PMException if there is an error traversing the graph.
     */
    Map<String, Set<String>> getCapabilityList(String subject, String process) throws PMException;

    /**
     * Given an Object Attribute, returns the name of every user (String), and what permissions(Set<String>) they have on it.
     *
     * @param target the name of the target node to generate the ACL for.
     * @param process the process requesting the ACL, can be null or empty
     * @return a map of the users that have access to the target node and the permissions each have on it
     */
    Map<String, Set<String>> generateACL(String target, String process) throws PMException;
}


