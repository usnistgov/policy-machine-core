package gov.nist.csd.pm.graph;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import gov.nist.csd.pm.graph.model.relationships.Assignment;
import gov.nist.csd.pm.graph.model.relationships.Association;
import gov.nist.csd.pm.graph.model.relationships.Relationship;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DirectedMultigraph;

import java.io.Serializable;
import java.util.*;

/**
 * MemGraph is an in-memory implementation of the graph interface.  It stores the IDs of the nodes in a DAG structure.
 * And stores all other node information in a map for easy/fast retrieval.
 */
public class MemGraph implements Graph, Serializable {

    private DirectedGraph<Long, Relationship> graph;
    private HashSet<Long>                     pcs;
    private HashMap<Long, NodeContext>        nodes;

    /**
     * Default constructor to create an empty graph in memory.
     */
    public MemGraph() {
        graph = new DirectedMultigraph<>(Relationship.class);
        nodes = new HashMap<>();
        pcs = new HashSet<>();
    }

    /**
     * Create a node in the in-memory graph.  The ID field of the passed Node must not be 0.
     * @param node the context of the node to create.  This includes the id, name, type, and properties.
     * @return the ID that was passed as part of the node parameter.
     * @throws IllegalArgumentException When the provided node is null.
     * @throws IllegalArgumentException When the provided node has an ID of 0.
     * @throws IllegalArgumentException When the provided node has a null or empty name.
     * @throws IllegalArgumentException When the provided node has a null type.
     */
    @Override
    public long createNode(NodeContext node) {
        //check for null values
        if(node == null) {
            throw new IllegalArgumentException("a null node was provided when creating a node in-memory");
        } else if(node.getID() == 0) {
            throw new IllegalArgumentException("no ID was provided when creating a node in the in-memory graph");
        } else if(node.getName() == null || node.getName().isEmpty()) {
            throw new IllegalArgumentException("no name was provided when creating a node in the in-memory graph");
        } else if(node.getType() == null) {
            throw new IllegalArgumentException("a null type was provided to the in memory graph when creating a node");
        }

        //if the node being created is a PC, add it to the graph and list of policies
        if (node.getType().equals(NodeType.PC)) {
            pcs.add(node.getID());
            graph.addVertex(node.getID());
        } else {
            graph.addVertex(node.getID());
        }

        //store the node in the map
        nodes.put(node.getID(), new NodeContext(node.getID(), node.getName(), node.getType(), node.getProperties()));

        //return the Node with the given info about the node
        return node.getID();
    }

    /**
     * Update a node with the given node context. Only the name and properties can be updated. If the name of the context
     * is null, then the name will not be updated.  The properties provided in the context will overwrite any existing
     * properties.  If the properties are null, they will be skipped. However, if the properties are an empty map, the
     * empty map will be set as the node's new properties.
     *
     * The ID must be present in order to identify which node to update.
     *
     * @param node the node to update. This includes the id, name, and properties.
     * @throws PMException if the given node ID does not exist in the graph.
     */
    @Override
    public void updateNode(NodeContext node) throws PMException {
        if(node == null) {
            throw new IllegalArgumentException("the node to update was null");
        }

        NodeContext existingNode = nodes.get(node.getID());
        if(existingNode == null) {
            throw new PMException(String.format("node with the ID %d could not be found to update", node.getID()));
        }

        // update name if present
        if(node.getName() != null && !node.getName().isEmpty()) {
            existingNode.name(node.getName());
        }

        // update the properties
        if(node.getProperties() != null) {
            existingNode.properties(node.getProperties());
        }

        // update the node information
        nodes.put(existingNode.getID(), existingNode);
    }

    /**
     * Delete the node with the given ID from the graph.  If the nodeID is 0 or does not already exist in the graph,
     * no errors will occur.
     * @param nodeID the ID of the node to delete.
     */
    @Override
    public void deleteNode(long nodeID) {
        NodeContext node = nodes.get(nodeID);
        //remove the vertex from the graph
        graph.removeVertex(nodeID);
        //remove the node from the policies if it is a policy class
        pcs.remove(nodeID);
        //remove the node from the map
        nodes.remove(nodeID);
    }

    @Override
    public boolean exists(long nodeID) {
        return graph.containsVertex(nodeID);
    }

    @Override
    public HashSet<Long> getPolicies() {
        return pcs;
    }

    @Override
    public Collection<NodeContext> getNodes() {
        return nodes.values();
    }

    /**
     * Retrieve the node from the graph with the given ID.
     * @param id the ID of the node to get.
     * @return the node with the given ID.
     * @throws PMException if the provided ID does not exist in the graph.
     */
    @Override
    public NodeContext getNode(long id) throws PMException {
        NodeContext node = nodes.get(id);
        if(node == null) {
            throw new PMException(String.format("a node with the ID %d does not exist", id));
        }

        return node;
    }

    /**
     * Search for nodes in the in-memory graph that match the given parameters. A node must match all parameters provided
     * including every property, to be included in the returned set.
     *
     * @param name the name of the nodes to search for.
     * @param type the type of the nodes to search for.
     * @param properties the properties of the nodes to search for.
     * @return the set of nodes that match the given parameters.
     */
    @Override
    public HashSet<NodeContext> search(String name, String type, Map<String, String> properties) {
        if(properties == null) {
            properties = new HashMap<>();
        }

        HashSet<NodeContext> results = new HashSet<>();
        // iterate over the nodes to find ones that match the search parameters
        for(NodeContext node : getNodes()) {
            // if the name parameter is not null and the current node name does not equal the name parameter, do not add
            if (name != null && !node.getName().equals(name)) {
                continue;
            }

            // if the type parameter is not null and the current node type does not equal the type parameter, do not add
            if (type != null && !node.getType().toString().equals(type)) {
                continue;
            }

            boolean add = true;
            for (String key : properties.keySet()) {
                String checkValue = properties.get(key);
                String foundValue = node.getProperties().get(key);
                // if the property provided in the search parameters is null or *, continue to the next property
                if(checkValue == null || checkValue.equals("*")) {
                    continue;
                }
                if(foundValue == null || !foundValue.equals(checkValue)) {
                    add = false;
                    break;
                }
            }

            if (add) {
                results.add(node);
            }
        }

        return results;
    }

    /**
     * Find all the nodes that are assigned to the given node.
     * @param nodeID the ID of the node to get the children of.
     * @return the set of nodes that are assigned to the given node.  The returned set will include each node's information provided in NodeContext objects.
     * @throws PMException if the provided nodeID does not exist in the graph.
     */
    @Override
    public HashSet<Long> getChildren(long nodeID) throws PMException {
        if(!exists(nodeID)) {
            throw new PMException(String.format("node %s does not exist in the graph", nodeID));
        }

        HashSet<Long> children = new HashSet<>();
        Set<Relationship> rels = graph.incomingEdgesOf(nodeID);
        for(Relationship rel : rels){
            if(rel instanceof Association) {
                continue;
            }
            children.add(rel.getSourceID());
        }
        return children;
    }

    /**
     * Find all the nodes that the given node is assigned to.
     * @param nodeID the ID of the node to get the parents of.
     * @return the set of nodes the given node is assigned to.  The returned set will include each node's information provided in NodeContext objects.
     * @throws PMException if the provided nodeID does not exist in the graph.
     */
    @Override
    public HashSet<Long> getParents(long nodeID) throws PMException {
        if(!exists(nodeID)) {
            throw new PMException(String.format("node %s does not exist in the graph", nodeID));
        }

        HashSet<Long> parents = new HashSet<>();
        Set<Relationship> rels = graph.outgoingEdgesOf(nodeID);
        for(Relationship rel : rels){
            if(rel instanceof Association) {
                continue;
            }
            parents.add(rel.getTargetID());
        }
        return parents;
    }

    /**
     * Assign the child node to the parent node.
     *
     * @param childCtx the context information for the child in the assignment.  The ID and type are required.
     * @param parentCtx the context information for the parent in the assignment The ID and type are required.
     * @throws IllegalArgumentException if the child node context is null.
     * @throws IllegalArgumentException if the parent node context is null.
     * @throws IllegalArgumentException if the child node does not exist in the graph.
     * @throws IllegalArgumentException if the parent node does not exist in the graph.
     */
    @Override
    public void assign(NodeContext childCtx, NodeContext parentCtx) {
        if(childCtx == null) {
            throw new IllegalArgumentException("child node context was null");
        } else if (parentCtx == null) {
            throw new IllegalArgumentException("parent node context was null");
        } else if(!exists(childCtx.getID())) {
            throw new IllegalArgumentException(String.format("node %s does not exist in the graph", childCtx));
        } else if(!exists(parentCtx.getID())) {
            throw new IllegalArgumentException(String.format("node %s does not exist in the graph", parentCtx));
        }

        graph.addEdge(childCtx.getID(), parentCtx.getID(), new Assignment(childCtx.getID(), parentCtx.getID()));
    }

    /**
     * Deassign the child node from the parent node.
     *
     * @param childCtx the context information for the child of the assignment.
     * @param parentCtx the context information for the parent of the assignment.
     * @throws IllegalArgumentException if the child node context is null.
     * @throws IllegalArgumentException if the parent node context is null.
     */
    @Override
    public void deassign(NodeContext childCtx, NodeContext parentCtx) {
        if(childCtx == null) {
            throw new IllegalArgumentException("child node context was null");
        } else if (parentCtx == null) {
            throw new IllegalArgumentException("parent node context was null");
        }

        graph.removeEdge(childCtx.getID(), parentCtx.getID());
    }

    /**
     * Associate the user attribute node and the target node.
     *
     * @param uaCtx the information for the user attribute in the association.
     * @param targetCtx the context information for the target of the association.
     * @param operations A Set of operations to add to the association.
     * @throws IllegalArgumentException if the user attribute node context is null.
     * @throws IllegalArgumentException if the target node context is null.
     * @throws PMException if the user attribute node does not exist in the graph.
     * @throws PMException if the target node does not exist in the graph.
     */
    @Override
    public void associate(NodeContext uaCtx, NodeContext targetCtx, HashSet<String> operations) throws PMException {
        if(uaCtx == null) {
            throw new IllegalArgumentException("user attribute node context was null");
        } else if (targetCtx == null) {
            throw new IllegalArgumentException("target node context was null");
        } else if(!exists(uaCtx.getID())) {
            throw new PMException(String.format("node %s does not exist in the graph", uaCtx.getID()));
        } else if(!exists(targetCtx.getID())) {
            throw new PMException(String.format("node %s does not exist in the graph", targetCtx.getID()));
        }

        if(graph.containsEdge(uaCtx.getID(), targetCtx.getID())) {
            // if the association exists update the operations
            Association assoc = (Association) graph.getEdge(uaCtx.getID(), targetCtx.getID());
            assoc.setOperations(operations);
        } else {
            graph.addEdge(uaCtx.getID(), targetCtx.getID(), new Association(uaCtx.getID(), targetCtx.getID(), operations));
        }
    }

    /**
     * Dissociate the user attribute node from the target node.  If an association does not exist, nothing happens.
     * @param uaCtx the context information for the user attribute of the association.
     * @param targetCtx the context information for the target of the association.
     */
    @Override
    public void dissociate(NodeContext uaCtx, NodeContext targetCtx) {
        graph.removeEdge(uaCtx.getID(), targetCtx.getID());
    }

    /**
     * Get the associations that the given node is the source of.
     *
     * @param sourceID the ID of the source node.
     * @return a map of the target nodes to the operations for each association that the given node is the source of.
     * @throws PMException if the given ID does not exist in the graph.
     */
    @Override
    public HashMap<Long, HashSet<String>> getSourceAssociations(long sourceID) throws PMException {
        if(!exists(sourceID)) {
            throw new PMException(String.format("node %s does not exist in the graph", sourceID));
        }

        HashMap<Long, HashSet<String>> assocs = new HashMap<>();
        Set<Relationship> rels = graph.outgoingEdgesOf(sourceID);
        for(Relationship rel : rels){
            if(rel instanceof Association){
                Association assoc = (Association) rel;
                assocs.put(assoc.getTargetID(), assoc.getOperations());
            }
        }
        return assocs;
    }

    /**
     * Get the associations that the given node is the target of.
     *
     * @param targetID the ID of the target node.
     * @return a map of the source nodes to the operations for each association that the given node is the target of.
     * @throws PMException if the given ID does not exist in the graph.
     */
    @Override
    public HashMap<Long, HashSet<String>> getTargetAssociations(long targetID) throws PMException {
        if(!exists(targetID)) {
            throw new PMException(String.format("node %s does not exist in the graph", targetID));
        }

        HashMap<Long, HashSet<String>> assocs = new HashMap<>();
        Set<Relationship> rels = graph.incomingEdgesOf(targetID);
        for(Relationship rel : rels){
            if(rel instanceof Association){
                Association assoc = (Association) rel;
                assocs.put(assoc.getSourceID(), assoc.getOperations());
            }
        }
        return assocs;
    }
}
