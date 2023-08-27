package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.List;
import java.util.Map;

/**
 * NGAC graph methods.
 */
public interface Graph {

    /**
     * Set the resource access rights recognized in this policy.
     *
     * @param accessRightSet The operations to set as the resource access rights.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    void setResourceAccessRights(AccessRightSet accessRightSet) throws PMException;

    /**
     * Get the resource access rights recognized by this policy.
     *
     * @return The resource access rights recognized by this policy.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    AccessRightSet getResourceAccessRights() throws PMException;

    /**
     * Create a policy class in the graph. This method should also create an object attribute that represents the
     * policy class in {@link gov.nist.csd.pm.pap.AdminPolicy#POLICY_CLASSES_OA}. This object attribute can be used in
     * the future to create associations with the policy class itself which is not a supported relation in NGAC. Any access
     * checks on the policy class will actually be executed on this representative object attribute.
     *
     * @param name The name of the policy class.
     * @return The name of the policy class.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    String createPolicyClass(String name, Map<String, String> properties) throws PMException;

    /**
     * Convenience method to create a policy class with no properties.
     *
     * @see Graph#createPolicyClass(String, Map)
     */
    String createPolicyClass(String name) throws PMException;

    /**
     * Create a new user attribute and assign it to the provided parent and optional additional parents.
     *
     * @param name    the name of the user attribute
     * @param parent  is the parent to initially assign the new node to.
     * @param parents is a list of 0 or more additional parents to assign the new node to.
     * @return the name of the user attribute.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents)
    throws PMException;

    /**
     * Convenience method to create a user attribute with no properties.
     *
     * @see Graph#createUserAttribute(String, Map, String, String...)
     */
    String createUserAttribute(String name, String parent, String... parents)
    throws PMException;

    /**
     * Create a new object attribute and assign it to the provided parent and optional additional parents.
     *
     * @param name    The name of the object attribute
     * @param parent  Is the parent to initially assign the new node to.
     * @param parents Is a list of 0 or more additional parents to assign the new node to.
     * @return The name of the object attribute.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents)
    throws PMException;

    /**
     * Convenience method to create an object attribute with no properties.
     *
     * @see Graph#createObjectAttribute(String, Map, String, String...)
     */
    String createObjectAttribute(String name, String parent, String... parents)
    throws PMException;

    /**
     * Create a new object and assign it to the provided parent and optional additional parents.
     *
     * @param name    The name of the object attribute
     * @param parent  Is the parent to initially assign the new node to.
     * @param parents Is a list of 0 or more additional parents to assign the new node to.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    String createObject(String name, Map<String, String> properties, String parent, String... parents)
    throws PMException;

    /**
     * Convenience method to create an object with no properties.
     *
     * @see Graph#createObject(String, Map, String, String...)
     */
    String createObject(String name, String parent, String... parents)
    throws PMException;

    /**
     * Create a new user and assign it to the provided parent and optional additional parents.
     *
     * @param name    The name of the object attribute
     * @param parent  Is the parent to initially assign the new node to.
     * @param parents Is a list of 0 or more additional parents to assign the new node to.
     * @return The name of the object attribute.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    String createUser(String name, Map<String, String> properties, String parent, String... parents)
    throws PMException;

    /**
     * Convenience method to create a user with no properties.
     *
     * @see Graph#createUser(String, Map, String, String...)
     */
    String createUser(String name, String parent, String... parents)
    throws PMException;

    /**
     * Update the properties of the node with the given name. The given properties overwrite any existing properties.
     *
     * @param name       The name of the node to update.
     * @param properties The properties to give the node.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    void setNodeProperties(String name, Map<String, String> properties)
            throws PMException;

    /**
     * Check if a node exists in the graph.
     *
     * @param name The name of the node to check for.
     * @return True if the node exists, false otherwise.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    boolean nodeExists(String name) throws PMException;

    /**
     * Get the Node object associated with the given name.
     *
     * @param name The name of the node to get.
     * @return The Node with the given name.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    Node getNode(String name) throws PMException;

    /**
     * Search for nodes with the given type and/or properties. To return all nodes, use type=NodeType.ANY and properties=new HashMap<>().
     * <p>
     * Supports wildcard property values i.e. {"prop1": "*"} which will match any nodes with the "prop1" property key.
     *
     * @param type       The type of nodes to search for. Use NodeType.ANY to search for any node type.
     * @param properties The properties of nodes to search for. An empty map will match all nodes.
     * @return The nodes that match the type and property criteria.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    List<String> search(NodeType type, Map<String, String> properties) throws PMException;

    /**
     * Get all policy class names.
     *
     * @return The names of all policy classes.
     */
    List<String> getPolicyClasses() throws PMException;

    /**
     * Delete the node with the given name from the graph. This will also delete the representative object attribute for
     * the policy class. An exception will be thrown if the node has any nodes assigned to it or if the node is defined
     * in a prohibition or an obligation event pattern. If the node does not exist, no exception will be thrown as this
     * is the desired state.
     *
     * @param name The name of the node to delete.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    void deleteNode(String name) throws PMException;

    /**
     * Assign the child node to the parent node. The child and parent nodes must both already exist in the graph,
     * and the types must make a valid assignment. If the child is already assigned to the parent, no exception will be
     * thrown as this is the desired state.
     *
     * @param child  The name of the child node.
     * @param parent The name of the parent node.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    void assign(String child, String parent) throws PMException;

    /**
     * Remove the assignment between the child and parent nodes. An exception will be thrown if either node
     * does not exist. If the assignment doesn't exist, no exception will be thrown as this is the desired state.
     *
     * @param child  The name of the child node.
     * @param parent The name of the parent node.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    void deassign(String child, String parent) throws PMException;

    /**
     * Assign all nodes in the given list to the target node.
     *
     * @param nodes The nodes to assign to the target.
     * @param target The target node to assign the nodes to.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    void assignAll(List<String> nodes, String target) throws PMException;

    /**
     * Deassign all nodes in the given list from the target node. If any of the assignments do not exist, no exception
     * will be thrown.
     *
     * @param nodes The nodes to deassign from the target.
     * @param target The target node to deassign the nodes from.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    void deassignAll(List<String> nodes, String target) throws PMException;

    /**
     * Deassign all children from the given node.
     *
     * @param target The target node to deassign it's children.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    void deassignAllFromAndDelete(String target) throws PMException;

    /**
     * Get the parents of the given node.
     *
     * @param node The node to get the parents of.
     * @return The names of the parents of the given node.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    List<String> getParents(String node) throws PMException;

    /**
     * Get the children of the given node.
     *
     * @param node The node to get the children of.
     * @return The names of the children of the given node.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    List<String> getChildren(String node) throws PMException;

    /**
     * Create an association between the user attribute and the target node with the provided access rights.
     * If an association already exists between these two nodes, overwrite the existing access rights with the ones
     * provided. Associations can only begin at a user attribute but can point to either an object or user attribute.
     *
     * @param ua The name of the user attribute.
     * @param target The name of the target attribute.
     * @param accessRights The set of access rights to add to the association.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    void associate(String ua, String target, AccessRightSet accessRights) throws PMException;

    /**
     * Delete the association between the user attribute and target node. If either of the nodes does not exist an exception
     * will be thrown. If the association does not exist no exception will be thrown as this is the desired state.
     *
     * @param ua The name of the user attribute.
     * @param target The name of the target attribute.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    void dissociate(String ua, String target) throws PMException;

    /**
     * Get the associations in which the given user attribute is the source.
     *
     * @param ua The user attribute to get the associations for.
     * @return The associations in which the source of the relation is the given user attribute.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    List<Association> getAssociationsWithSource(String ua) throws PMException;

    /**
     * Get the associations in which the given node is the target.
     *
     * @param target The target attribute to get the associations for.
     * @return The associations in which the target of the relation is the given node.
     * @throws PMException If there is an error during the execution process related to the policy machine implementation.
     */
    List<Association> getAssociationsWithTarget(String target) throws PMException;
}
