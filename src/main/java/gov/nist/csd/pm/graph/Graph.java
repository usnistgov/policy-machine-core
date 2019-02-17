package gov.nist.csd.pm.graph;

import gov.nist.csd.pm.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMGraphException;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Interface for maintaining an NGAC graph.
 */
public interface Graph {

    /**
     * Create a new node with the given name, type and properties and add it to the graph.
     * @param node the context of the node to create.  This includes the id, name, type, and properties.
     * @return the ID of the newly created node.
     * @throws PMDBException if there is an error creating the node in a database.
     * @throws PMGraphException if there is an error accessing the graph.
     */
    long createNode(NodeContext node) throws PMDBException, PMGraphException;

    /**
     * Update the name and properties of the node with the given ID. The node's existing properties will be overwritten 
     * by the ones provided. The name parameter is optional and will be ignored if null or empty.  The properties 
     * parameter will be ignored only if null.  If the map is empty, the node's properties will be overwritten
     * with the empty map.
     * @param node the node to update. This includes the id, name, and properties.
     * @throws PMDBException if there is an error creating the node in a database.
     * @throws PMGraphException if there is an error accessing the graph.
     */
    void updateNode(NodeContext node) throws PMDBException, PMGraphException;

    /**
     * Delete the node with the given ID from the graph.
     * @param nodeID the ID of the node to delete.
     * @throws PMDBException if there is an error creating the node in a database.
     * @throws PMGraphException if there is an error accessing the graph.
     */
    void deleteNode(long nodeID) throws PMDBException, PMGraphException;

    /**
     * Check that a node with the given ID exists in the graph.
     * @param nodeID the ID of the node to check for.
     * @return true or False if a node with the given ID exists or not.
     * @throws PMDBException if there is an error creating the node in a database.
     * @throws PMGraphException if there is an error accessing the graph.
     */
    boolean exists(long nodeID) throws PMDBException, PMGraphException;

    /**
     * Get the set of policy classes.  This operation is run every time a decision is made, so a separate
     * method is needed to improve efficiency. The returned set is just the IDs of each policy class.
     * @return the set of policy class IDs.
     * @throws PMDBException if there is an error creating the node in a database.
     * @throws PMGraphException if there is an error accessing the graph.
     */
    HashSet<Long> getPolicies() throws PMDBException, PMGraphException;

    /**
     * Get the set of nodes that are assigned to the node with the given ID.
     * @param nodeID the ID of the node to get the children of.
     * @return the Set of NGACNodes that are assigned to the node with the given ID.
     * @throws PMDBException if there is an error creating the node in a database.
     * @throws PMGraphException if there is an error accessing the graph.
     */
    HashSet<NodeContext> getChildren(long nodeID) throws PMDBException, PMGraphException;

    /**
     * Get the set of nodes that the node with the given ID is assigned to.
     * @param nodeID the ID of the node to get the parents of.
     * @return the Set of NGACNodes that are assigned to the node with the given ID.
     * @throws PMDBException if there is an error creating the node in a database.
     * @throws PMGraphException if there is an error accessing the graph.
     */
    HashSet<NodeContext> getParents(long nodeID) throws PMDBException, PMGraphException;

    /**
     * Assign the child node to the parent node. The child and parent nodes must both already exist in the graph,
     * and the types must make a valid assignment. An example of a valid assignment is assigning o1, an object, to oa1,
     * an object attribute.  o1 is the child (objects can never be the parent in an assignment), and oa1 is the parent.
     *
     * @param childCtx the context information for the child in the assignment.  The ID and type are required.
     * @param parentCtx the context information for the parent in the assignment The ID and type are required.
     * @throws PMDBException if there is an error creating the node in a database.
     * @throws PMGraphException if there is an error accessing the graph.
     */
    void assign(NodeContext childCtx, NodeContext parentCtx) throws PMDBException, PMGraphException;

    /**
     * Remove the Assignment between the child and parent nodes.
     * @param childCtx the context information for the child of the assignment.
     * @param parentCtx the context information for the parent of the assignment.
     * @throws PMDBException if there is an error creating the node in a database.
     * @throws PMGraphException if there is an error accessing the graph.
     */
    void deassign(NodeContext childCtx, NodeContext parentCtx) throws PMDBException, PMGraphException;

    /**
     * Create an Association between the user attribute and the Target node with the provided operations. If an association
     * already exists between these two nodes, overwrite the existing operations with the ones provided.  Associations
     * can only begin at a user attribute but can point to either an Object or user attribute
     *
     * @param uaCtx the information for the user attribute in the association.
     * @param targetCtx the context information for the target of the association.
     * @param operations A Set of operations to add to the association.
     * @throws PMDBException if there is an error creating the node in a database.
     * @throws PMGraphException if there is an error accessing the graph.
     */
    void associate(NodeContext uaCtx, NodeContext targetCtx, HashSet<String> operations) throws PMDBException, PMGraphException;

    /**
     * Delete the Association between the user attribute and Target node.
     * @param uaCtx the context information for the user attribute of the association.
     * @param targetCtx the context information for the target of the association.
     * @throws PMDBException if there is an error creating the node in a database.
     * @throws PMGraphException if there is an error accessing the graph.
     */
    void dissociate(NodeContext uaCtx, NodeContext targetCtx) throws PMDBException, PMGraphException;

    /**
     * Retrieve the associations the given node is the source of.  The source node of an association is always a
     * user attribute and this method will throw an exception if an invalid node is provided.  The returned Map will
     * contain the target and operations of each association.
     * @param sourceID the ID of the source node.
     * @return a Map of the target node IDs and the operations for each association.
     * @throws PMDBException if there is an error creating the node in a database.
     * @throws PMGraphException if there is an error accessing the graph.
     */
    HashMap<Long, HashSet<String>> getSourceAssociations(long sourceID) throws PMDBException, PMGraphException;

    /**
     * Retrieve the associations the given node is the target of.  The target node can be an Object Attribute or a User
     * Attribute. This method will throw an exception if a node of any other type is provided.  The returned Map will
     * contain the source node IDs and the operations of each association.
     * @param targetID the ID of the target node.
     * @return a Map of the source Ids and the operations for each association.
     * @throws PMDBException if there is an error creating the node in a database.
     * @throws PMGraphException if there is an error accessing the graph.
     */
    HashMap<Long, HashSet<String>> getTargetAssociations(long targetID) throws PMDBException, PMGraphException;
}
