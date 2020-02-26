package gov.nist.csd.pm.pip.graph;

import gov.nist.csd.pm.epp.EPPOptions;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.graph.model.relationships.Assignment;
import gov.nist.csd.pm.pip.graph.model.relationships.Association;
import gov.nist.csd.pm.pip.graph.model.relationships.Relationship;
import gov.nist.csd.pm.pip.obligations.MemObligations;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.jgrapht.DirectedGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.graph.DirectedMultigraph;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;

/**
 * MemGraph is an in-memory implementation of the graph interface.  It stores the IDs of the nodes in a DAG structure.
 * And stores all other node information in a map for easy/fast retrieval.
 */
public class MemGraph implements Graph {

    private static final String NODE_NOT_FOUND_MSG = "node %s does not exist in the graph";

    private DirectedGraph<Long, Relationship> graph;
    private HashSet<Long>                     pcs;
    private HashMap<Long, Node>               nodes;
    private HashMap<Integer, Long>             names;

    /**
     * Default constructor to create an empty graph in memory.
     */
    public MemGraph() {
        graph = new DirectedAcyclicGraph<>(Relationship.class);
        nodes = new HashMap<>();
        names = new HashMap<>();
        pcs = new HashSet<>();
    }

    @Override
    public Node createPolicyClass(long id, String name, Map<String, String> properties) throws PMException {
        if (exists(id)) {
            throw new IllegalArgumentException("the ID already exists in the graph");
        }
        else if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("no name was provided when creating a node in the in-memory graph");
        }

        Node node = new Node(id, name, PC, properties);

        // add the pc's ID to the pc set and to the graph
        pcs.add(id);
        graph.addVertex(id);
        names.put(node.hashCode(), id);

        // create the node
        nodes.put(id, node);

        return node;
    }

    /**
     * Create a node in the in-memory graph.  The ID field of the passed Node must not be 0.
     *
     * @return the ID that was passed as part of the node parameter.
     * @throws IllegalArgumentException When the provided node is null.
     * @throws IllegalArgumentException When the provided node has an ID of 0.
     * @throws IllegalArgumentException When the provided node has a null or empty name.
     * @throws IllegalArgumentException When the provided node has a null type.
     */

    @Override
    public Node createNode(long id, String name, NodeType type, Map<String, String> properties, long initialParent, long... additionalParents) throws PMException {
        //check for null values
        if (type == PC) {
            throw new PMException("use createPolicyClass to create a policy class node");
        }
        else if (exists(id)) {
            throw new IllegalArgumentException("the ID already exists in the graph");
        }
        else if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("no name was provided when creating a node in the in-memory graph");
        }
        else if (type == null) {
            throw new IllegalArgumentException("a null type was provided to the in memory graph when creating a node");
        }
        else if (initialParent == 0) {
            throw new IllegalArgumentException("must specify an initial parent ID when creating a non policy class node");
        }

        Node node = new Node(id, name, type, properties);

        // check that the name type pairing doesnt already exist
        if (names.containsKey(node.hashCode())) {
            throw new PMException("a node with the name " + name + " already exists");
        }

        // add the vertex to the graph
        graph.addVertex(id);
        names.put(node.hashCode(), id);

        //store the node in the map
        nodes.put(id, node);

        // assign the new node the to given parent nodes
        assign(id, initialParent);
        for (long parentID : additionalParents) {
            assign(id, parentID);
        }

        //return the Node
        return node;
    }

    /**
     * Update a node with the given node context. Only the name and properties can be updated. If the name of the context
     * is null, then the name will not be updated.  The properties provided in the context will overwrite any existing
     * properties.  If the properties are null, they will be skipped. However, if the properties are an empty map, the
     * empty map will be set as the node's new properties.
     * <p>
     * The ID must be present in order to identify which node to update.
     *
     * @throws PMException if the given node ID does not exist in the graph.
     */
    @Override
    public void updateNode(long id, String name, Map<String, String> properties) throws PMException {
        Node existingNode = nodes.get(id);
        if (existingNode == null) {
            throw new PMException(String.format("node with the ID %d could not be found to update", id));
        }

        // update name if present
        if (name != null && !name.isEmpty()) {
            names.remove(existingNode.hashCode());
            existingNode.setName(name);
            names.put(existingNode.hashCode(), existingNode.getID());
        }

        // update the properties
        if (properties != null) {
            existingNode.setProperties(properties);
        }

        // update the node information
        nodes.put(existingNode.getID(), existingNode);
    }

    /**
     * Delete the node with the given ID from the graph.  If the nodeID is 0 or does not already exist in the graph,
     * no errors will occur.
     *
     * @param nodeID the ID of the node to delete.
     */
    @Override
    public void deleteNode(long nodeID) throws PMException {
        Node node = getNode(nodeID);
        //remove the vertex from the graph
        graph.removeVertex(nodeID);
        //remove the node from the policies if it is a policy class
        pcs.remove(nodeID);
        //remove the node from the map
        nodes.remove(nodeID);
        names.remove(node.getName());
    }

    @Override
    public boolean exists(long nodeID) {
        return graph.containsVertex(nodeID);
    }

    @Override
    public Set<Long> getPolicyClasses() {
        return pcs;
    }

    @Override
    public Set<Node> getNodes() {
        return new HashSet<>(nodes.values());
    }

    /**
     * Retrieve the node from the graph with the given ID.
     *
     * @param id the ID of the node to get.
     * @return the node with the given ID.
     * @throws PMException if the provided ID does not exist in the graph.
     */
    @Override
    public Node getNode(long id) throws PMException {
        Node node = nodes.get(id);
        if (node == null) {
            throw new PMException(String.format("a node with the ID %d does not exist", id));
        }

        return node;
    }

    @Override
    public Node getNode(String name, NodeType type, Map<String, String> properties) throws PMException {
        Set<Node> search = search(name, type, properties);
        if (search.isEmpty()) {
            throw new PMException(String.format("a node matching the criteria (%s, %s, %s) does not exist", name, type, properties));
        }
        return search.iterator().next();
    }

    /**
     * Search for nodes in the in-memory graph that match the given parameters. A node must match all parameters provided
     * including every property, to be included in the returned set.
     *
     * @param name       the name of the nodes to search for.
     * @param type       the type of the nodes to search for.
     * @param properties the properties of the nodes to search for.
     * @return the set of nodes that match the given parameters.
     */
    @Override
    public Set<Node> search(String name, NodeType type, Map<String, String> properties) {
        if (properties == null) {
            properties = new HashMap<>();
        }

        HashSet<Node> results = new HashSet<>();
        // iterate over the nodes to find ones that match the search parameters
        for (Node node : getNodes()) {
            // if the name parameter is not null and the current node name does not equal the name parameter, do not add
            // if the type parameter is not null and the current node type does not equal the type parameter, do not add
            if (name != null && !node.getName().equals(name) ||
                    type != null && !node.getType().equals(type)) {
                continue;
            }

            boolean add = true;
            for (String key : properties.keySet()) {
                String checkValue = properties.get(key);
                String foundValue = node.getProperties().get(key);
                // if the property provided in the search parameters is null or *, continue to the next property
                if (!(checkValue == null || checkValue.equals("*")) &&
                        (foundValue == null || !foundValue.equals(checkValue))) {
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
     *
     * @param nodeID the ID of the node to get the children of.
     * @return the set of nodes that are assigned to the given node.  The returned set will include each node's information provided in Node objects.
     * @throws PMException if the provided nodeID does not exist in the graph.
     */
    @Override
    public Set<Long> getChildren(long nodeID) throws PMException {
        if (!exists(nodeID)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, nodeID));
        }

        HashSet<Long> children = new HashSet<>();
        Set<Relationship> rels = graph.incomingEdgesOf(nodeID);
        for (Relationship rel : rels) {
            if (rel instanceof Association) {
                continue;
            }
            children.add(rel.getSourceID());
        }
        return children;
    }

    /**
     * Find all the nodes that the given node is assigned to.
     *
     * @param nodeID the ID of the node to get the parents of.
     * @return the set of nodes the given node is assigned to.  The returned set will include each node's information provided in Node objects.
     * @throws PMException if the provided nodeID does not exist in the graph.
     */
    @Override
    public Set<Long> getParents(long nodeID) throws PMException {
        if (!exists(nodeID)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, nodeID));
        }

        HashSet<Long> parents = new HashSet<>();
        Set<Relationship> rels = graph.outgoingEdgesOf(nodeID);
        for (Relationship rel : rels) {
            if (rel instanceof Association) {
                continue;
            }
            parents.add(rel.getTargetID());
        }
        return parents;
    }

    /**
     * Assign the child node to the parent node. Both nodes must exist and both types must make a valid assignment.
     *
     * @throws IllegalArgumentException if the child node context is null.
     * @throws IllegalArgumentException if the parent node context is null.
     * @throws IllegalArgumentException if the child node does not exist in the graph.
     * @throws IllegalArgumentException if the parent node does not exist in the graph.
     * @throws PMException if the two types do not make a valid assignment.
     */
    @Override
    public void assign(long childID, long parentID) throws PMException {
        if (!exists(childID)) {
            throw new IllegalArgumentException(String.format(NODE_NOT_FOUND_MSG, childID));
        }
        else if (!exists(parentID)) {
            throw new IllegalArgumentException(String.format(NODE_NOT_FOUND_MSG, parentID));
        }

        if (graph.containsEdge(childID, parentID)) {
            throw new PMException(childID + " is already assigned to" + parentID);
        }

        Node child = getNode(childID);
        Node parent = getNode(parentID);

        Assignment.checkAssignment(child.getType(), parent.getType());

        graph.addEdge(childID, parentID, new Assignment(childID, parentID));
    }

    /**
     * Deassign the child node from the parent node.
     *
     * @throws IllegalArgumentException if the child node context is null.
     * @throws IllegalArgumentException if the parent node context is null.
     */
    @Override
    public void deassign(long childID, long parentID) {
        graph.removeEdge(childID, parentID);
    }

    @Override
    public boolean isAssigned(long childID, long parentID) throws PMException {
        return graph.containsEdge(childID, parentID);
    }

    /**
     * Associate the user attribute node and the target node.
     *
     * @throws IllegalArgumentException if the user attribute node context is null.
     * @throws IllegalArgumentException if the target node context is null.
     * @throws PMException              if the user attribute node does not exist in the graph.
     * @throws PMException              if the target node does not exist in the graph.
     */
    @Override
    public void associate(long uaID, long targetID, OperationSet operations) throws PMException {
        if (!exists(uaID)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, uaID));
        }
        else if (!exists(targetID)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, targetID));
        }

        Node ua = getNode(uaID);
        Node target = getNode(targetID);

        // check that the association is valid
        Association.checkAssociation(ua.getType(), target.getType());

        // if no edge exists create an association
        // if an assignment exists create a new edge for the association
        // if an association exists update it
        Relationship edge = graph.getEdge(uaID, targetID);
        if (edge == null || edge instanceof Assignment) {
            graph.addEdge(uaID, targetID, new Association(uaID, targetID, operations));
        }
        else if (edge instanceof Association) {
            Association assoc = (Association) graph.getEdge(uaID, targetID);
            assoc.setOperations(operations);
        }
    }

    /**
     * Dissociate the user attribute node from the target node.  If an association does not exist, nothing happens.
     */
    @Override
    public void dissociate(long uaID, long targetID) {
        graph.removeEdge(uaID, targetID);
    }

    /**
     * Get the associations that the given node is the source of.
     *
     * @param sourceID the ID of the source node.
     * @return a map of the target nodes to the operations for each association that the given node is the source of.
     * @throws PMException if the given ID does not exist in the graph.
     */
    @Override
    public Map<Long, OperationSet> getSourceAssociations(long sourceID) throws PMException {
        if (!exists(sourceID)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, sourceID));
        }

        Map<Long, OperationSet> assocs = new HashMap<>();
        Set<Relationship> rels = graph.outgoingEdgesOf(sourceID);
        for (Relationship rel : rels) {
            if (rel instanceof Association) {
                Association assoc = (Association) rel;
                assocs.put(assoc.getTargetID(), new OperationSet(assoc.getOperations()));
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
    public Map<Long, OperationSet> getTargetAssociations(long targetID) throws PMException {
        if (!exists(targetID)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, targetID));
        }

        Map<Long, OperationSet> assocs = new HashMap<>();
        Set<Relationship> rels = graph.incomingEdgesOf(targetID);
        for (Relationship rel : rels) {
            if (rel instanceof Association) {
                Association assoc = (Association) rel;
                assocs.put(assoc.getSourceID(), new OperationSet(assoc.getOperations()));
            }
        }
        return assocs;
    }
}
