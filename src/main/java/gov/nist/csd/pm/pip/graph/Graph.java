package gov.nist.csd.pm.pip.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.util.Map;
import java.util.Set;

/**
 * Interface for maintaining an NGAC graph.
 */
public interface Graph {

    /**
     * Create a policy class in the graph.
     *
     * @param name the name of the policy class.
     * @param properties the properties of the policy class.
     * @return the node representing the new policy class.
     * @throws PMException if there is an error creating the policy class node in the graph.
     */
    Node createPolicyClass(String name, Map<String, String> properties) throws PMException;

    /**
     * Create a new node with the given name, type and properties and add it to the graph. Node names must be unique.
     *
     * @param name the unique name of the node.
     * @param type the type of node.
     * @param properties any additional properties to store in the node.
     * @param initialParent is the parent to initially assign the new node to.  A node needs to be connected to the
     *                      graph when created.
     * @param additionalParents is a list of 0 or more additional parents to assign the new node to.
     * @return the Node representation of the newly created node.
     * @throws PMException if there is an error creating the node in the graph.
     */
    Node createNode(String name, NodeType type, Map<String, String> properties, String initialParent, String ... additionalParents) throws PMException;

    /**
     * Update the properties of the node with the given name. The given properties overwrite any existing properties.
     *
     * @param name the name of the node to update.
     * @param properties the properties to give the node.
     * @throws PMException if there is an error updating the node in the graph.
     */
    void updateNode(String name, Map<String, String> properties) throws PMException;

    /**
     * Delete the node with the given name from the graph. The node must not have any other nodes assigned to it.
     *
     * @param name the name of the node to delete.
     * @throws PMException if the node being deleted still has other nodes assigned to it.
     * @throws PMException if there is an error deleting the node from the graph.
     */
    void deleteNode(String name) throws PMException;

    /**
     * Check that a node with the given name exists in the graph.
     *
     * @param name the name of the node to check for.
     * @return true or false if a node with the given name exists or not.
     * @throws PMException if there is an error checking if the node exists in the graph.
     */
    boolean exists(String name) throws PMException;

    /**
     * Get the set of policy classes.  This operation is run every time a decision is made, so a separate
     * method is needed to improve efficiency. The returned set is just the names of each policy class.
     *
     * @return the set of policy classes.
     * @throws PMException if there is an error retrieving the policy classes.
     */
    Set<String> getPolicyClasses() throws PMException;

    /**
     * Retrieve the set of all nodes in the graph.
     *
     * @return a Set of all the nodes in the graph.
     * @throws PMException if there is an error retrieving all nodes in the graph.
     */
    Set<Node> getNodes() throws PMException;

    /**
     * Retrieve the node with the given name.
     *
     * @param name the name of the node to retrieve
     * @return the Node with the given name.
     * @throws PMException if there is an error retrieving the node from the graph.
     */
    Node getNode(String name) throws PMException;

    /**
     * Search the graph for a node that matches the given parameters. A node must
     * contain all properties provided to be returned.
     * To get a node that has a specific property key with any value use "*" as the value in the parameter.
     * (i.e. {key=*})
     * If more than one node matches the criteria, only one will be returned.
     *
     * @param type       the type of the nodes to search for.
     * @param properties the properties of the nodes to search for.
     * @return the node that matches the given search criteria.
     * @throws PMException if there is an error searching the graph.
     */
    Node getNode(NodeType type, Map<String, String> properties) throws PMException;

    /**
     * Search the graph for nodes matching the given parameters. A node must
     * contain all properties provided to be returned.
     * To get all the nodes that have a specific property key with any value use "*" as the value in the parameter.
     * (i.e. {key=*})
     *
     * @param type       the type of the nodes to search for.
     * @param properties the properties of the nodes to search for.
     * @return a set of nodes that match the given search criteria.
     * @throws PMException if there is an error searching the graph.
     */
    Set<Node> search(NodeType type, Map<String, String> properties) throws PMException;

    /**
     * Get the set of nodes that are assigned to the node with the given name.
     *
     * @param name the name of the node to get the children of.
     * @return the Set of NGACNodes that are assigned to the node with the given name.
     * @throws PMException if there is an error retrieving the children of the node.
     */
    Set<String> getChildren(String name) throws PMException;

    /**
     * Get the set of nodes that the node with the given name is assigned to.
     *
     * @param name the name of the node to get the parents of.
     * @return the set of node names that are assigned to the node with the given name.
     * @throws PMException if there is an error retrieving the parents of the node.
     */
    Set<String> getParents(String name) throws PMException;

    /**
     * Assign the child node to the parent node. The child and parent nodes must both already exist in the graph,
     * and the types must make a valid assignment. An example of a valid assignment is assigning o1, an object, to oa1,
     * an object attribute.  o1 is the child (objects can never be the parent in an assignment), and oa1 is the parent.
     *
     * @param child  the name of the child node.
     * @param parent the name of the parent node.
     * @throws PMException if there is an error assigning the two nodes.
     */
    void assign(String child, String parent) throws PMException;

    /**
     * Remove the Assignment between the child and parent nodes.
     *
     * @param child  the name of the child node.
     * @param parent the name of the parent node.
     * @throws PMException if there is an error deassigning the two nodes.
     */
    void deassign(String child, String parent) throws PMException;


    /**
     * Returns true if the child is assigned to the parent.
     *
     * @param child the name of the child node
     * @param parent the name of the parent node
     * @return true if the child is assigned to the parent, false otherwise
     * @throws PMException if there is an error checking if the child is assigned to the parent
     */
    boolean isAssigned(String child, String parent) throws PMException;

    /**
     * Create an Association between the user attribute and the Target node with the provided operations. If an association
     * already exists between these two nodes, overwrite the existing operations with the ones provided.  Associations
     * can only begin at a user attribute but can point to either an Object or user attribute
     *
     * @param ua the name of the user attribute.
     * @param target the name of the target attribute.
     * @param operations a set of operations to add to the association.
     * @throws PMException if there is an error associating the two nodes.
     */
    void associate(String ua, String target, OperationSet operations) throws PMException;

    /**
     * Delete the Association between the user attribute and Target node.
     *
     * @param ua     the name of the user attribute.
     * @param target the name of the target attribute.
     * @throws PMException if there is an error dissociating the two nodes.
     */
    void dissociate(String ua, String target) throws PMException;

    /**
     * Retrieve the associations the given node is the source of.  The source node of an association is always a
     * user attribute and this method will throw an exception if an invalid node is provided.  The returned Map will
     * contain the target and operations of each association.
     *
     * @param source the name of the source node.
     * @return a map of the target node names and the operations for each association.
     * @throws PMException if there is an retrieving the associations of the source node from the graph.
     */
    Map<String, OperationSet> getSourceAssociations(String source) throws PMException;

    /**
     * Retrieve the associations the given node is the target of.  The target node can be an Object Attribute or a User
     * Attribute. This method will throw an exception if a node of any other type is provided.  The returned Map will
     * contain the source node names and the operations of each association.
     *
     * @param target the name of the target node.
     * @return a Map of the source names and the operations for each association.
     * @throws PMException if there is an retrieving the associations of the target node from the graph.
     */
    Map<String, OperationSet> getTargetAssociations(String target) throws PMException;

    /**
     * Convert the graph to a json string with the format:
     * {
     *   "nodes": [
     *     {
     *       "name": "pc1",
     *       "type": "PC",
     *       "properties": {}
     *     },
     *     ...
     *   ],
     *   "assignments": [
     *     ["child1", "parent1"],
     *     ["child1", "parent2"],
     *     ...
     *   ],
     *   "associations": [
     *     {
     *       "operations": [
     *         "read",
     *         "write"
     *       ],
     *       "source": "ua",
     *       "target": "oa"
     *     }
     *   ]
     * }
     *
     * @return the json string representation of the graph
     */
    String toJson() throws PMException;

    /**
     * Load a json string representation of a graph into the current graph.
     * @param s the string representation of the graph
     */
    void fromJson(String s) throws PMException;
}
