package gov.nist.csd.pm.pap.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.exception.*;

import java.util.Collection;
import java.util.Map;

/**
 * NGAC graph methods.
 */
public interface GraphModification {

    /**
     * Create a policy class in the graph.
     *
     * @param name The name of the policy class.
     * @return The name of the policy class.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    String createPolicyClass(String name) throws PMException;

    /**
     * Create a new user attribute and assign it to the provided assignments. User attributes must have at
     * least one assignment initially.
     *
     * @param name    the name of the user attribute
     * @param assignments A list of assignments to assign the new node to.
     *
     * @return the name of the user attribute.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    String createUserAttribute(String name, Collection<String> assignments) throws PMException;

    /**
     * Create a new object attribute and assign it to the provided assignments. Object attributes must have at
     * least one assignment initially.
     *
     * @param name    The name of the object attribute
     * @param assignments A list of 0 or more assignments to assign the new node to.
     *
     * @return The name of the object attribute.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    String createObjectAttribute(String name, Collection<String> assignments) throws PMException;

    /**
     * Create a new object and assign it to the provided assignments. Objects must have a least one assignment initially.
     *
     * @param name    The name of the object attribute
     * @param assignments A list of 0 or more assignments to assign the new node to.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    String createObject(String name, Collection<String> assignments) throws PMException;

    /**
     * Create a new user and assign it to the provided assignments. Users must have a least one assignment initially.
     *
     * @param name    The name of the object attribute
     * @param assignments A list of 0 or more assignments to assign the new node to.
     *
     * @return The name of the object attribute.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    String createUser(String name, Collection<String> assignments) throws PMException;

    /**
     * Update the properties of the node with the given name. The given properties overwrite any existing properties.
     *
     * @param name       The name of the node to update.
     * @param properties The properties to give the node.
     * @throws PMBackendException If there is an error executing the command in the PIP.
     */
    void setNodeProperties(String name, Map<String, String> properties) throws PMException;

    /**
     * Delete the node with the given name from the graph. If the node is a policy class this will also delete the
     * representative object attribute. An exception will be thrown if the node has any nodes assigned to it or if
     * the node is defined in a prohibition or an obligation event pattern. If the node does not exist, no exception
     * will be thrown as this is the desired state.
     *
     * @param name The name of the node to delete.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    void deleteNode(String name) throws PMException;

    /**
     * Assign the ascendant node to the descendant node.
     *
     * @param ascendant   The name of the ascendant node.
     * @param descendants The names of the descendant nodes.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    void assign(String ascendant, Collection<String> descendants) throws PMException;

    /**
     * Delete the assignment between the ascendant and descendant nodes.
     *
     * @param ascendant   The name of the ascendant node.
     * @param descendants The names of the descendant nodes.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    void deassign(String ascendant, Collection<String> descendants) throws PMException;

    /**
     * Create an association between the user attribute and the target node with the provided access rights.
     * If an association already exists between these two nodes, overwrite the existing access rights with the ones
     * provided. Associations can only begin at a user attribute but can point to either an object or user attribute. If
     * either node does not exist or a provided access right is unknown to the policy an exception will be thrown.
     *
     * @param ua The name of the user attribute.
     * @param target The name of the target attribute.
     * @param accessRights The set of access rights to add to the association.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    void associate(String ua, String target, AccessRightSet accessRights) throws PMException;

    /**
     * Delete the association between the user attribute and target node.  If either of the nodes does not exist an
     * exception will be thrown. If the association does not exist no exception will be thrown as this is the desired
     * state.
     *
     * @param ua The name of the user attribute.
     * @param target The name of the target attribute.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    void dissociate(String ua, String target) throws PMException;

}
