package gov.nist.csd.pm.pip.memory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.common.FunctionalEntity;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;

import gov.nist.csd.pm.pap.GraphAdmin;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.graph.model.relationships.Assignment;
import gov.nist.csd.pm.pip.graph.model.relationships.Association;
import gov.nist.csd.pm.pip.graph.model.relationships.Relationship;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DirectedMultigraph;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.PC;

/**
 * MemGraph is an in-memory implementation of the graph interface.  It stores the names of the nodes in a DAG structure.
 * And stores all other node information in a map for easy/fast retrieval.
 */
public class MemGraph implements Graph {

    private static final String NODE_NOT_FOUND_MSG = "node %s does not exist in the graph";

    protected DirectedGraph<String, Relationship> graph;
    protected HashSet<String>                     pcs;
    protected HashMap<String, Node>               nodes;

    /**
     * Default constructor to create an empty graph in memory.
     */
    public MemGraph() {
        graph = new DirectedMultigraph<>(Relationship.class);
        nodes = new HashMap<>();
        pcs = new HashSet<>();
    }

    @Override
    public synchronized Node createPolicyClass(String name, Map<String, String> properties) throws PMException {
        if (name == null) {
            throw new PMException("no name was provided when creating a node in the in-memory graph");
        } else if (exists(name)) {
            throw new PMException("the name " + name + " already exists in the graph");
        }

        // add the pc's name to the pc set and to the graph
        pcs.add(name);
        graph.addVertex(name);

        // create the node
        Node node = new Node(name, PC, properties);
        nodes.put(name, node);

        return node;
    }

    /**
     * Create a node in the in-memory graph.
     *
     * @return the Node that was created.
     * @throws PMException when the provided name is null.
     * @throws PMException when the provided name already exists in the graph.
     * @throws PMException when the provided type is null.
     * @throws PMException when an initial parent is not provided.
     */

    @Override
    public synchronized Node createNode(String name, NodeType type, Map<String, String> properties, String initialParent, String... additionalParents) throws PMException {//check for null values
        if (type == PC) {
            throw new PMException("use createPolicyClass to create a policy class node");
        } else if (name == null) {
            throw new PMException("no name was provided when creating a node in the in-memory graph");
        } else if (exists(name)) {
            throw new PMException("the name " + name + " already exists in the graph");
        } else if (type == null) {
            throw new PMException("a null type was provided to the in memory graph when creating a node");
        } else if (initialParent == null) {
            throw new PMException("must specify an initial parent when creating a non policy class node");
        }

        // add the vertex to the graph
        graph.addVertex(name);

        //store the node in the map
        Node node = new Node(name, type, properties);
        nodes.put(name, node);

        // assign the new node the to given parent nodes
        assign(name, initialParent);
        for (String parent : additionalParents) {
            assign(name, parent);
        }

        //return the Node
        return node;
    }

    /**
     * Create a node in the in-memory graph without parents (for import/export)
     *
     * @throws IllegalArgumentException when the provided name is null.
     * @throws IllegalArgumentException when the provided name already exists in the graph.
     * @throws IllegalArgumentException when the provided type is null.
     * @throws IllegalArgumentException when an initial parent is not provided.
     */
    private void createNode(String name, NodeType type, Map<String, String> properties) throws PMException {
        //check for null values
        if (type == PC) {
            throw new PMException("use createPolicyClass to create a policy class node");
        }
        else if (name == null) {
            throw new IllegalArgumentException("no name was provided when creating a node in the in-memory graph");
        }
        else if (exists(name)) {
            throw new IllegalArgumentException("the name " + name + " already exists in the graph");
        }
        else if (type == null) {
            throw new IllegalArgumentException("a null type was provided to the in memory graph when creating a node");
        }

        // add the vertex to the graph
        graph.addVertex(name);

        //store the node in the map
        Node node = new Node(name, type, properties);
        nodes.put(name, node);
    }

    /**
     * Update a node with the given node context. Only the name and properties can be updated. If the name of the context
     * is null, then the name will not be updated.  The properties provided in the context will overwrite any existing
     * properties.  If the properties are null, they will be skipped. However, if the properties are an empty map, the
     * empty map will be set as the node's new properties.
     * <p>
     * The name must be present in order to identify which node to update.
     *
     * @throws PMException if the given node name does not exist in the graph.
     */
    @Override
    public synchronized void updateNode(String name, Map<String, String> properties) throws PMException {Node existingNode = nodes.get(name);
        if (existingNode == null) {
            throw new PMException(String.format("node with the name %s could not be found to update", name));
        }

        // update the properties
        if (properties != null) {
            existingNode.setProperties(properties);
        }

        // update the node information
        nodes.put(existingNode.getName(), existingNode);
    }

    /**
     * Delete the node with the given name from the graph.  If the name is empty or does not already exist in the graph,
     * no errors will occur.
     *
     * @param name the name of the node to delete.
     */
    @Override
    public synchronized void deleteNode(String name) throws PMException {
        if (graph.incomingEdgesOf(name).size() != 0) {
            throw new PMException("cannot delete " + name + ", nodes are still assigned to it");
        }

        //remove the vertex from the graph
        graph.removeVertex(name);
        //remove the node from the policies if it is a policy class
        pcs.remove(name);
        //remove the node from the map
        nodes.remove(name);
    }

    @Override
    public synchronized boolean exists(String name) {
        return graph.containsVertex(name);
    }

    @Override
    public synchronized Set<String> getPolicyClasses() {
        return new HashSet<>(pcs);
    }

    @Override
    public synchronized Set<Node> getNodes() {
        Collection<Node> nodes = this.nodes.values();
        Set<Node> nodeSet = new HashSet<>();
        for (Node node : nodes) {
            nodeSet.add(new Node(node));
        }
        return nodeSet;
    }

    /**
     * Retrieve the node from the graph with the given name.
     *
     * @param name the name of the node to get.
     * @return the node with the given name.
     * @throws PMException if the provided name does not exist in the graph.
     */
    @Override
    public synchronized Node getNode(String name) throws PMException {
        Node node = nodes.get(name);
        if (node == null) {
            throw new PMException(String.format("a node with the name %s does not exist", name));
        }

        return new Node(node);
    }

    @Override
    public synchronized Node getNode(NodeType type, Map<String, String> properties) throws PMException {
        Set<Node> search = search(type, properties);
        if (search.isEmpty()) {
            throw new PMException(String.format("a node matching the criteria (%s, %s) does not exist", type, properties));
        }

        return new Node(search.iterator().next());
    }

    /**
     * Search for nodes in the in-memory graph that match the given parameters. A node must match all parameters provided
     * including every property, to be included in the returned set.
     *
     * @param type       the type of the nodes to search for.
     * @param properties the properties of the nodes to search for.
     * @return the set of nodes that match the given parameters.
     */
    @Override
    public synchronized Set<Node> search(NodeType type, Map<String, String> properties) {
        if (properties == null) {
            properties = new HashMap<>();
        }

        HashSet<Node> results = new HashSet<>();
        // iterate over the nodes to find ones that match the search parameters
        for (Node node : getNodes()) {
            // if the type parameter is not null and the current node type does not equal the type parameter, do not add
            if (type != null && !node.getType().equals(type)) {
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
                results.add(new Node(node));
            }
        }

        return results;
    }

    /**
     * Find all the nodes that are assigned to the given node.
     *
     * @param name the name of the node to get the children of.
     * @return the set of nodes that are assigned to the given node.  The returned set will include each node's information provided in Node objects.
     * @throws PMException if the provided name does not exist in the graph.
     */
    @Override
    public synchronized Set<String> getChildren(String name) throws PMException {
        if (!exists(name)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, name));
        }

        HashSet<String> children = new HashSet<>();
        Set<Relationship> rels = graph.incomingEdgesOf(name);
        for (Relationship rel : rels) {
            if (rel instanceof Association) {
                continue;
            }
            children.add(rel.getSource());
        }
        return children;
    }

    /**
     * Find all the nodes that the given node is assigned to.
     *
     * @param name the name of the node to get the parents of.
     * @return the set of nodes the given node is assigned to.  The returned set will include each node's information provided in Node objects.
     * @throws PMException if the provided name does not exist in the graph.
     */
    @Override
    public synchronized Set<String> getParents(String name) throws PMException {
        if (!exists(name)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, name));
        }

        HashSet<String> parents = new HashSet<>();
        Set<Relationship> rels = graph.outgoingEdgesOf(name);
        for (Relationship rel : rels) {
            if (rel instanceof Association) {
                continue;
            }
            parents.add(rel.getTarget());
        }
        return parents;
    }

    /**
     * Assign the child node to the parent node. Both nodes must exist and both types must make a valid assignment.
     *
     * @throws PMException if the child node context is null.
     * @throws PMException if the parent node context is null.
     * @throws PMException if the child node does not exist in the graph.
     * @throws PMException if the parent node does not exist in the graph.
     * @throws PMException if the two types do not make a valid assignment.
     */
    @Override
    public synchronized void assign(String child, String parent) throws PMException {
        if (!exists(child)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, child));
        } else if (!exists(parent)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, parent));
        }

        if (graph.containsEdge(child, parent)) {
            throw new PMException(child + " is already assigned to" + parent);
        }

        Node childNode = getNode(child);
        Node parentNode = getNode(parent);

        Assignment.checkAssignment(childNode.getType(), parentNode.getType());

        graph.addEdge(child, parent, new Assignment(child, parent));
    }

    /**
     * Deassign the child node from the parent node.
     *
     * @throws PMException if the child node context is null.
     * @throws PMException if the parent node context is null.
     */
    @Override
    public synchronized void deassign(String child, String parent) {
        graph.removeEdge(new Assignment(child, parent));
    }

    @Override
    public synchronized boolean isAssigned(String child, String parent) throws PMException {
        return graph.containsEdge(new Assignment(child, parent));
    }

    /**
     * Associate the user attribute node and the target node. If an association already exists, the operations will
     * be updated with the given operations.
     *
     * @throws PMException              if the user attribute node does not exist in the graph.
     * @throws PMException              if the target node does not exist in the graph.
     */
    @Override
    public synchronized void associate(String ua, String target, OperationSet operations) throws PMException {
        if (!exists(ua)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, ua));
        } else if (!exists(target)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, target));
        }

        Node uaNode = getNode(ua);
        Node targetNode = getNode(target);

        // check that the association is valid
        Association.checkAssociation(uaNode.getType(), targetNode.getType());

        // if no edge exists create an association
        // if an assignment exists create a new edge for the association
        // if an association exists update it
        Relationship edge = graph.getEdge(ua, target);
        if (edge == null || edge instanceof Assignment) {
            graph.addEdge(ua, target, new Association(ua, target, operations));
        } else if (edge instanceof Association) {
            Association assoc = (Association) graph.getEdge(ua, target);
            assoc.setOperations(operations);
        }
    }

    /**
     * Dissociate the user attribute node from the target node.  If an association does not exist, nothing happens.
     */
    @Override
    public synchronized void dissociate(String ua, String target) {
        graph.removeEdge(new Association(ua, target));
    }

    /**
     * Get the associations that the given node is the source of.
     *
     * @param source the name of the source node.
     * @return a map of the target nodes to the operations for each association that the given node is the source of.
     * @throws PMException if the given name does not exist in the graph.
     */
    @Override
    public synchronized Map<String, OperationSet> getSourceAssociations(String source) throws PMException {
        if (!exists(source)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, source));
        }

        Map<String, OperationSet> assocs = new HashMap<>();
        Set<Relationship> rels = graph.outgoingEdgesOf(source);
        for (Relationship rel : rels) {
            if (rel instanceof Association) {
                Association assoc = (Association) rel;
                assocs.put(assoc.getTarget(), new OperationSet(assoc.getOperations()));
            }
        }
        return assocs;
    }

    /**
     * Get the associations that the given node is the target of.
     *
     * @param target the name of the target node.
     * @return a map of the source nodes to the operations for each association that the given node is the target of.
     * @throws PMException if the given name does not exist in the graph.
     */
    @Override
    public synchronized Map<String, OperationSet> getTargetAssociations(String target) throws PMException {
        if (!exists(target)) {
            throw new PMException(String.format(NODE_NOT_FOUND_MSG, target));
        }

        Map<String, OperationSet> assocs = new HashMap<>();
        Set<Relationship> rels = graph.incomingEdgesOf(target);
        for (Relationship rel : rels) {
            if (rel instanceof Association) {
                Association assoc = (Association) rel;
                assocs.put(assoc.getSource(), new OperationSet(assoc.getOperations()));
            }
        }
        return assocs;
    }

    @Override
    public synchronized String toJson() throws PMException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Collection<Node> nodes = this.getNodes().stream().filter(
                node -> !node.getName().equalsIgnoreCase("super_pc")
                        && !node.getName().equalsIgnoreCase("super_ua1")
                        && !node.getName().equalsIgnoreCase("super_ua2")
                        && !node.getName().equalsIgnoreCase("super_oa")
                        && !node.getName().equalsIgnoreCase("super")
                        && !node.getName().equalsIgnoreCase("super_pc_default_UA")
                        && !node.getName().equalsIgnoreCase("super_pc_default_OA")
                        && !node.getName().equalsIgnoreCase("super_pc_rep")
                        && !node.getName().contains("_default_UA")
                        && !node.getName().contains("_default_OA")
                        && !node.getName().contains("_rep"))
                .collect(Collectors.toList());
        HashSet<String[]> jsonAssignments = new HashSet<>();
        HashSet<JsonAssociation> jsonAssociations = new HashSet<>();
        for (Node node : nodes) {

            Set<String> parents = this.getParents(node.getName());

            for (String parent : parents) {
                jsonAssignments.add(new String[]{node.getName(), parent});
            }

            Map<String, OperationSet> associations = this.getSourceAssociations(node.getName());
            for (String target : associations.keySet()) {
                OperationSet ops = associations.get(target);
                Node targetNode = this.getNode(target);

                jsonAssociations.add(new JsonAssociation(node.getName(), targetNode.getName(), ops));
            }
        }

        return gson.toJson(new JsonGraph(nodes, jsonAssignments, jsonAssociations));
    }

    @Override
    public synchronized void fromJson(String json) throws PMException {
            JsonGraph jsonGraph = new Gson().fromJson(json, JsonGraph.class);

            Collection<Node> nodes = jsonGraph.getNodes().stream().filter(
                    node -> !node.getName().equalsIgnoreCase("super_pc")
                            && !node.getName().equalsIgnoreCase("super_ua1")
                            && !node.getName().equalsIgnoreCase("super_ua2")
                            && !node.getName().equalsIgnoreCase("super_oa")
                            && !node.getName().equalsIgnoreCase("super")
                            && !node.getName().equalsIgnoreCase("super_pc_default_UA")
                            && !node.getName().equalsIgnoreCase("super_pc_default_OA")
                            && !node.getName().equalsIgnoreCase("super_pc_rep"))
                    .collect(Collectors.toList());
            for (Node node : nodes) {
                if (node.getType().equals(PC)) {
                    //use createpolicyClass from GraphService/ GraphAdmin instead of the class method
                    this.createPolicyClass(node.getName(), node.getProperties());
                } else {
                /*this.graph.addVertex(node.getName());
                this.nodes.put(node.getName(), node);*/
                    this.createNode(node.getName(), node.getType(), node.getProperties());
                }
            }

            List<String[]> assignments = new ArrayList<>(jsonGraph.getAssignments());
            for (String[] assignment : assignments) {
                if (assignment.length != 2) {
                    throw new PMException("invalid assignment (format=[child, parent]): " + Arrays.toString(assignment));
                }

                String source = assignment[0];
                String target = assignment[1];

                this.assign(source, target);
            }

            Set<JsonAssociation> associations = jsonGraph.getAssociations();
            for (JsonAssociation association : associations) {
                String ua = association.getSource();
                String target = association.getTarget();
                this.associate(ua, target, new OperationSet(association.getOperations()));
            }
        }

        public void fromJson_with_config (String json) throws PMException {
            JsonGraph jsonGraph = new Gson().fromJson(json, JsonGraph.class);

            Collection<Node> nodes = jsonGraph.getNodes();
            for (Node node : nodes) {
                if (node.getType().equals(PC)) {
                    this.createPolicyClass(node.getName(), node.getProperties());
                } else {
                    this.createNode(node.getName(), node.getType(), node.getProperties());
                }
            }

            List<String[]> assignments = new ArrayList<>(jsonGraph.getAssignments());
            for (String[] assignment : assignments) {
                if (assignment.length != 2) {
                    throw new PMException("invalid assignment (format=[child, parent]): " + Arrays.toString(assignment));
                }

                String source = assignment[0];
                String target = assignment[1];

                this.assign(source, target);
            }

            Set<JsonAssociation> associations = jsonGraph.getAssociations();
            for (JsonAssociation association : associations) {
                String ua = association.getSource();
                String target = association.getTarget();
                this.associate(ua, target, new OperationSet(association.getOperations()));
            }
        }


    private static class JsonGraph {
        Collection<Node> nodes;
        Set<String[]>  assignments;
        Set<JsonAssociation> associations;

        JsonGraph(Collection<Node> nodes, Set<String[]> assignments, Set<JsonAssociation> associations) {
            this.nodes = nodes;
            this.assignments = assignments;
            this.associations = associations;
        }

        Collection<Node> getNodes() {
            return nodes;
        }

        Set<String[]> getAssignments() {
            return assignments;
        }

        Set<JsonAssociation> getAssociations() {
            return associations;
        }
    }

    private static class JsonAssociation {
        String source;
        String target;
        Set<String> operations;

        public JsonAssociation(String source, String target, Set<String> operations) {
            this.source = source;
            this.target = target;
            this.operations = operations;
        }

        public String getSource() {
            return source;
        }

        public String getTarget() {
            return target;
        }

        public Set<String> getOperations() {
            return operations;
        }
    }
}
