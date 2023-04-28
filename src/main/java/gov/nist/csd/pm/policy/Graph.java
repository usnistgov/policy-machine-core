package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.List;
import java.util.Map;

public interface Graph {

    /**
     * Set the resource access rights recognized in this policy.
     * @param accessRightSet the operations to set as the resource access rights
     */
    void setResourceAccessRights(AccessRightSet accessRightSet) throws PMException;

    /**
     * Get the resource access rights recognized by this policy.
     * @return The resource access rights recognized by this policy.
     * @throws PMException
     */
    AccessRightSet getResourceAccessRights() throws PMException;

    /**
     * Create a policy class in the graph.
     *
     * @param name the name of the policy class.
     * @return the node representing the new policy class.
     * @throws PMException
     */
    String createPolicyClass(String name, Map<String, String> properties) throws PMException;
    String createPolicyClass(String name) throws PMException;

    /**
     * Create a new user attribute. User attributes do not have to be connected to the graph, therefore
     * the parents argument is optional.
     * @param name the name of the suer attribute
     * @param parent is the parent to initially assign the new node to.  A user attribute needs to be connected
     *                      to the graph when created.
     * @param parents is a list of 0 or more additional parents to assign the new node to.
     * @return the user attribute node
     * @throws PMException
     */
    String createUserAttribute(String name, Map<String, String> properties, String parent, String ... parents) throws PMException;
    String createUserAttribute(String name, String parent, String ... parents) throws PMException;

    /**
     * Create a new object attribute with the given name, and assign it to the given parents.
     * Note: Node names must be unique.
     *
     * @param name the unique name of the node.
     * @param parent is the parent to initially assign the new node to.  An object attribute needs to be connected
     *                      to the graph when created.
     * @param parents is a list of 0 or more additional parents to assign the new node to.
     * @return the object attribute node
     * @throws PMException
     */
    String createObjectAttribute(String name, Map<String, String> properties, String parent, String ... parents) throws PMException;
    String createObjectAttribute(String name, String parent, String ... parents) throws PMException;

    /**
     * Create a new object with the given name, and assign it to the given parents.
     * Note: Node names must be unique.
     *
     * @param name the unique name of the node.
     * @param parent is the parent to initially assign the new node to.  An object needs to be connected to the
     *                      graph when created.
     * @param parents is a list of 0 or more additional parents to assign the new node to.
     * @return the object node
     * @throws PMException
     */
    String createObject(String name, Map<String, String> properties, String parent, String ... parents) throws PMException;
    String createObject(String name, String parent, String ... parents) throws PMException;

    /**
     * Create a new user with the given name, and assign it to the given parents.
     * Note: Node names must be unique.
     *
     * @param name the unique name of the node.
     * @param parent is the parent to initially assign the new node to.  A user needs to be connected to the
     *                      graph when created.
     * @param parents is a list of 0 or more additional parents to assign the new node to.
     * @return the user node
     * @throws PMException
     */
    String createUser(String name, Map<String, String> properties, String parent, String ... parents) throws PMException;
    String createUser(String name, String parent, String ... parents) throws PMException;

    /**
     * Update the properties of the node with the given name. The given properties overwrite any existing properties.
     *
     * @param name the name of the node to update.
     * @param properties the properties to give the node.
     * @throws PMException
     */
    void setNodeProperties(String name, Map<String, String> properties) throws PMException;

    /**
     * Check if a node exists in the graph.
     * @param name The name of the node to check for.
     * @return True if the node exists, false otherwise.
     * @throws PMException
     */
    boolean nodeExists(String name) throws PMException;

    /**
     * Get the Node object associated with the given name.
     * @param name The name of the node to get.
     * @return A Node object associated with the given name.
     * @throws PMException
     */
    Node getNode(String name) throws PMException;

    /**
     * Search for nodes with the given type and/or properties.
     * Supports wildcard property values i.e. {"prop1": "*"} which will match any nodes with the "prop1" property key.
     * @param type The type of nodes to search for. Use NodeType.ANY to search for any node type.
     * @param properties The properties of nodes to search for.
     * @return The nodes that match the type and property criteria.
     * @throws PMException
     */
    List<String> search(NodeType type, Map<String, String> properties) throws PMException;

    /**
     * Get the policy classes in the policy.
     * @return The policy class nodes.
     * @throws PMException
     */
    List<String> getPolicyClasses() throws PMException;

    /**
     * Delete the node with the given name from the graph. The node must not have any other nodes assigned to it.
     * If the node does not exist, no exception will be thrown as this is the desired state.
     *
     * @param name the name of the node to delete.
     * @throws PMException
     */
    void deleteNode(String name) throws PMException;

    /**
     * Assign the child node to the parent node. The child and parent nodes must both already exist in the graph,
     * and the types must make a valid assignment. An example of a valid assignment is assigning o1, an object, to oa1,
     * an object attribute.  o1 is the child (objects can never be the parent in an assignment), and oa1 is the parent.
     * If the child is already assigned to the parent, no exception will be thrown as this is the desired state.
     *
     * @param child  the name of the child node.
     * @param parent the name of the parent node.
     * @throws PMException
     */
    void assign(String child, String parent) throws PMException;

    /**
     * Remove the assignment between the child and parent nodes. An exception will be thrown if either node
     * does not exist. If the assignment doesn't exist, no exception will be thrown as this is the desired state.
     *
     * @param child  the name of the child node.
     * @param parent the name of the parent node.
     * @throws PMException
     */
    void deassign(String child, String parent) throws PMException;

    /**
     * Assign the children of the given attribute to the target attribute of the same type.
     * @param children The children to assign to the target.
     * @param target The target attribute to assign the children to.
     * @throws PMException
     */
    void assignAll(List<String> children, String target) throws PMException;

    /**
     * Deassign the children of the given attribute from the target attribute of the same type.
     * @param children The children to deassign from the given target.
     * @param target The target attribute to deassign the children from.
     * @throws PMException
     */
    void deassignAll(List<String> children, String target) throws PMException;

    /**
     * Deassign all nodes assigned to the given node and delete the given node.
     * from the rest of the graph.
     * @param target The node to deassign all children from.
     * @throws PMException
     */
    void deassignAllFromAndDelete(String target) throws PMException;

    /**
     * Get the parents of the given node.
     * @param node The node to get the parents of.
     * @return The names of the parents of the given node.
     * @throws PMException
     */
    List<String> getParents(String node) throws PMException;

    /**
     * Get the children of the given node.
     * @param node The node to get the children of.
     * @return The names of the children of the given node.
     * @throws PMException
     */
    List<String> getChildren(String node) throws PMException;

    /**
     * Create an Association between the user attribute and the Target node with the provided access rights. If an association
     * already exists between these two nodes, overwrite the existing access rights with the ones provided.  Associations
     * can only begin at a user attribute but can point to either an object or user attribute
     *
     * @param ua the name of the user attribute.
     * @param target the name of the target attribute.
     * @param accessRights a set of access rights to add to the association.
     * @throws PMException
     */
    void associate(String ua, String target, AccessRightSet accessRights) throws PMException;

    /**
     * Delete the Association between the user attribute and Target node.
     *
     * @param ua     the name of the user attribute.
     * @param target the name of the target attribute.
     * @throws PMException
     */
    void dissociate(String ua, String target) throws PMException;

    /**
     * Get the associations in which the source of the relation is the given user attribute (ua).
     * @param ua The user attribute to get the associations for.
     * @return The associations in which the source of the relation is the given user attribute (ua).
     * @throws PMException
     */
    List<Association> getAssociationsWithSource(String ua) throws PMException;

    /**
     * Get the associations in which the target of the relation is the given node.
     * @param target The target attribute to get the associations for.
     * @return The associations in which the target of the relation is the given node.
     * @throws PMException
     */
    List<Association> getAssociationsWithTarget(String target) throws PMException;
}
