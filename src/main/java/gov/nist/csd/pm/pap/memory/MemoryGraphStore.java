package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.GraphStore;
import gov.nist.csd.pm.policy.exceptions.TransactionNotStartedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.exceptions.NodeNameExistsException;
import gov.nist.csd.pm.policy.model.graph.Graph;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.graph.relationships.Relationship;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.*;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.WILDCARD;

class MemoryGraphStore extends GraphStore {

    private Graph graph;

    private TxHandler<Graph> txHandler;

    MemoryGraphStore() {
        this.graph = new Graph();
        this.txHandler = new TxHandler<>();
    }

    MemoryGraphStore(Graph graph) {
        this.graph = copyGraph(graph);
        this.txHandler = new TxHandler<>();
    }

    MemoryGraphStore(MemoryGraphStore graph) {
        this.graph = copyGraph(graph.graph);
        this.txHandler = new TxHandler<>();
    }

    private Graph copyGraph(Graph toCopy) {
        DirectedAcyclicGraph<String, Relationship> copyDag = new DirectedAcyclicGraph<>(Relationship.class);
        DirectedAcyclicGraph<String, Relationship> dag = toCopy.getGraph();
        Set<String> vertexes = dag.vertexSet();
        for (String vertex : vertexes) {
            copyDag.addVertex(vertex);
        }
        Set<Relationship> edges = dag.edgeSet();
        for (Relationship edge : edges) {
            Relationship relation;
            if (edge.isAssociation()) {
                relation = new Relationship(edge.getSource(), edge.getTarget(), new AccessRightSet(edge.getAccessRightSet()));
            } else {
                relation = new Relationship(edge.getSource(), edge.getTarget());
            }

            copyDag.addEdge(edge.getSource(), edge.getTarget(), relation);
        }
        HashMap<String, Node> copyNodes = new HashMap<>(toCopy.getNodes());
        List<String> copyPcs = new ArrayList<>(toCopy.getPcs());

        return new Graph(copyDag, copyNodes, copyPcs, new AccessRightSet(toCopy.getResourceAccessRights()));
    }

    Graph getGraph() {
        return graph;
    }

    @Override
    public synchronized void setResourceAccessRights(AccessRightSet accessRightSet) {
        graph.setResourceAccessRights(accessRightSet);
    }

    @Override
    public synchronized AccessRightSet getResourceAccessRights() {
        return new AccessRightSet(graph.getResourceAccessRights());
    }

    @Override
    public synchronized String createPolicyClass(String name, Map<String, String> properties) {
        graph.addNode(name, PC, properties);
        return name;
    }

    @Override
    public synchronized String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        return createNode(name, UA, properties, parent, parents);
    }

    @Override
    public synchronized String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        return createNode(name, OA, properties, parent, parents);
    }

    @Override
    public synchronized String createObject(String name, Map<String, String> properties, String parent, String... parents) {
        return createNode(name, O, properties, parent, parents);
    }

    @Override
    public synchronized String createUser(String name, Map<String, String> properties, String parent, String... parents) {
        return createNode(name, U, properties, parent, parents);
    }

    private synchronized String createNode(String name, NodeType type, Map<String, String> properties, String initialParent, String ... parents) {
        graph.addNode(name, type, properties);
        graph.addAssignmentEdge(name, initialParent);
        for (String parent : parents) {
            graph.addAssignmentEdge(name, parent);
        }

        return name;
    }

    @Override
    public synchronized void setNodeProperties(String name, Map<String, String> properties) throws NodeNameExistsException {
        graph.setNodeProperties(name, properties);
    }

    @Override
    public synchronized void deleteNode(String name) {
        graph.deleteNode(name);
    }

    @Override
    public synchronized boolean nodeExists(String name) {
        return graph.containsNode(name);
    }

    @Override
    public synchronized Node getNode(String name) {
        Node node = graph.getNode(name);
        return new Node(node);
    }

    @Override
    public synchronized List<String> search(NodeType type, Map<String, String> checkProperties) {
        List<String> results = new ArrayList<>();
        // iterate over the nodes to find ones that match the search parameters
        for (String name : graph.getNodes().keySet()) {
            Node node = graph.getNode(name);
            // if the type parameter is not null and the current node type does not equal the type parameter, do not add
            if (type != null && !node.getType().equals(type)) {
                continue;
            }

            Map<String, String> nodeProperties = node.getProperties();

            if (!hasAllKeys(nodeProperties, checkProperties)) {
                continue;
            } else if (!valuesMatch(nodeProperties, checkProperties)) {
                continue;
            }


            results.add(node.getName());
        }

        return results;
    }

    private boolean valuesMatch(Map<String, String> nodeProperties, Map<String, String> checkProperties) {
        for (String checkKey : checkProperties.keySet()) {
            String checkValue = checkProperties.get(checkKey);
            if (!checkValue.equals(nodeProperties.get(checkKey))
                    && !checkValue.equals(WILDCARD)) {
                return false;
            }
        }

        return true;
    }

    private boolean hasAllKeys(Map<String, String> nodeProperties, Map<String, String> checkProperties) {
        for (String key : checkProperties.keySet()) {
            if (!nodeProperties.containsKey(key)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public synchronized List<String> getPolicyClasses() {
        return new ArrayList<>(graph.getPcs());
    }

    @Override
    public synchronized void assign(String child, String parent) {
        graph.addAssignmentEdge(child, parent);
    }

    @Override
    public synchronized void deassign(String child, String parent) {
        graph.removeEdge(child, parent);
    }

    @Override
    public synchronized List<String> getChildren(String node) {
        return graph.getIncomingAssignmentEdges(node);
    }

    @Override
    public synchronized List<String> getParents(String node) {
        return graph.getOutgoingAssignmentEdges(node);
    }

    @Override
    public synchronized void associate(String ua, String target, AccessRightSet accessRights) {
        if (graph.containsEdge(ua, target)) {
            // remove the existing association edge in order to update it
            graph.removeEdge(ua, target);
        }

        graph.addAssociationEdge(ua, target, accessRights);
    }

    @Override
    public synchronized void dissociate(String ua, String target) {
        graph.removeEdge(ua, target);
    }

    @Override
    public synchronized List<Association> getAssociationsWithSource(String ua) {
        return graph.getOutgoingAssociationEdges(ua);
    }

    @Override
    public synchronized List<Association> getAssociationsWithTarget(String target) {
        return graph.getIncomingAssociationEdges(target);
    }

    @Override
    public synchronized void beginTx() {
        if (!txHandler.isInTx()) {
            txHandler.setState(copyGraph(graph));
        }

        txHandler.beginTx();
    }

    @Override
    public synchronized void commit() throws TransactionNotStartedException {
        if (!txHandler.isInTx()) {
            throw new TransactionNotStartedException();
        }

        txHandler.commit();
    }

    @Override
    public synchronized void rollback() throws TransactionNotStartedException {
        if (!txHandler.isInTx()) {
            return;
        }

        graph = txHandler.getState();
        txHandler.rollback();
    }
}
