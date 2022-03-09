package gov.nist.csd.pm.policy.model.graph;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.graph.relationships.Relationship;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.*;

public class Graph {

    private final DirectedAcyclicGraph<String, Relationship> graph;
    private final HashMap<String, Node> nodes;
    private final List<String> pcs;
    private AccessRightSet resourceAccessRights;

    public Graph() {
        this.graph = new DirectedAcyclicGraph<>(Relationship.class);
        this.nodes = new HashMap<>();
        this.pcs = new ArrayList<>();
        this.resourceAccessRights = new AccessRightSet();
    }

    public Graph(DirectedAcyclicGraph<String, Relationship> graph, HashMap<String, Node> nodes,
                 List<String> pcs, AccessRightSet resourceAccessRights) {
        this.graph = graph;
        this.nodes = nodes;
        this.pcs = pcs;
        this.resourceAccessRights = resourceAccessRights;
    }

    public AccessRightSet getResourceAccessRights() {
        return resourceAccessRights;
    }

    public void setResourceAccessRights(AccessRightSet resourceAccessRights) {
        this.resourceAccessRights = resourceAccessRights;
    }

    public DirectedAcyclicGraph<String, Relationship> getGraph() {
        return graph;
    }

    public HashMap<String, Node> getNodes() {
        return nodes;
    }

    public List<String> getPcs() {
        return pcs;
    }

    public String addNode(String name, NodeType type, Map<String, String> properties) {
        this.graph.addVertex(name);
        this.nodes.put(name, new Node(name, type, properties));
        if (type == NodeType.PC) {
            this.pcs.add(name);
        }

        return name;
    }

    public void setNodeProperties(String name, Map<String, String> properties) {
        Node node = this.nodes.get(name);
        if (node == null) {
            return;
        }

        node.setProperties(properties);
        this.nodes.put(name, node);
    }

    public Node getNode(String name) {
        return this.nodes.get(name);
    }

    public void deleteNode(String name) {
        if (this.graph.containsVertex(name)) {
            this.graph.removeVertex(name);
        }

        nodes.remove(name);
        pcs.remove(name);
    }

    public boolean containsNode(String node) {
        return graph.containsVertex(node);
    }

    public void addAssignmentEdge(String source, String target) {
        graph.addEdge(source, target, new Relationship(source, target));
    }

    public void addAssociationEdge(String source, String target, AccessRightSet set) {
        graph.addEdge(source, target, new Relationship(source, target, set));
    }

    public void removeEdge(String source, String target) {
        graph.removeEdge(source, target);
    }

    public List<String> getIncomingAssignmentEdges(String node) {
        Set<Relationship> rels = graph.incomingEdgesOf(node);
        List<String> edges = new ArrayList<>();
        for (Relationship rel : rels) {
            if (rel.isAssociation()) {
                continue;
            }

            edges.add(rel.getSource());
        }
        return edges;
    }

    public List<String> getOutgoingAssignmentEdges(String node) {
        Set<Relationship> rels = graph.outgoingEdgesOf(node);
        List<String> edges = new ArrayList<>();
        for (Relationship rel : rels) {
            if (rel.isAssociation()) {
                continue;
            }

            edges.add(rel.getTarget());
        }
        return edges;
    }

    public List<Association> getIncomingAssociationEdges(String node) {
        Set<Relationship> rels = graph.incomingEdgesOf(node);
        List<Association> edges = new ArrayList<>();
        for (Relationship rel : rels) {
            if (!rel.isAssociation()) {
                continue;
            }

            edges.add(new Association(rel.getSource(), rel.getTarget(), rel.getAccessRightSet()));
        }
        return edges;
    }

    public List<Association> getOutgoingAssociationEdges(String node) {
        Set<Relationship> rels = graph.outgoingEdgesOf(node);
        List<Association> edges = new ArrayList<>();
        for (Relationship rel : rels) {
            if (!rel.isAssociation()) {
                continue;
            }

            edges.add(new Association(rel.getSource(), rel.getTarget(), rel.getAccessRightSet()));
        }
        return edges;
    }

    public boolean containsEdge(String source, String target) {
        return graph.containsEdge(source, target);
    }

    public boolean isEdgeAssociation(String source, String target) {
        if(!graph.containsEdge(source, target)) {
            return false;
        }

        return graph.getEdge(source, target).isAssociation();
    }
}
