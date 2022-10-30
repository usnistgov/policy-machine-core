package gov.nist.csd.pm.policy.model.graph;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.*;

public class Graph {

    private final Map<String, Vertex> graph;
    private final List<String> pcs;
    private final AccessRightSet resourceAccessRights;

    public Graph() {
        this.graph = new HashMap<>();
        this.pcs = new ArrayList<>();
        this.resourceAccessRights = new AccessRightSet();
    }

    public Graph(Map<String, Vertex> nodes, List<String> pcs, AccessRightSet resourceAccessRights) {
        this.graph = nodes;
        this.pcs = pcs;
        this.resourceAccessRights = resourceAccessRights;
    }

    public Graph(Graph graph) {
        this.graph = new HashMap<>();
        for (String n : graph.graph.keySet()) {
            this.graph.put(n, graph.graph.get(n).copy());
        }

        this.pcs = new ArrayList<>(graph.pcs);
        this.resourceAccessRights = new AccessRightSet(graph.getResourceAccessRights());
    }

    public AccessRightSet getResourceAccessRights() {
        return resourceAccessRights;
    }

    public void setResourceAccessRights(AccessRightSet resourceAccessRights) {
        this.resourceAccessRights.clear();
        this.resourceAccessRights.addAll(resourceAccessRights);
    }

    public Map<String, Vertex> getGraph() {
        return graph;
    }

    public List<String> getPcs() {
        return pcs;
    }

    public String addNode(String name, NodeType type, Map<String, String> properties) {
        this.graph.put(name, getVertex(name, type, properties));
        if (type == NodeType.PC) {
            this.pcs.add(name);
        }

        return name;
    }

    private Vertex getVertex(String name, NodeType type, Map<String, String> properties) {
        switch (type){
            case PC -> {
                return new PolicyClass(name, properties);
            }
            case OA -> {
                return new ObjectAttribute(name, properties);
            }
            case UA -> {
                return new UserAttribute(name, properties);
            }
            case O -> {
                return new Object(name, properties);
            }
            default -> {
                return new User(name, properties);
            }
        }
    }

    public void setNodeProperties(String name, Map<String, String> properties) {
        this.graph.get(name).setProperties(properties);
    }

    public Node getNode(String name) {
        return this.graph.get(name).getNode();
    }

    public void deleteNode(String name) {
        if (!graph.containsKey(name)) {
            return;
        }

        Vertex vertex = graph.get(name);

        List<String> children = vertex.getChildren();
        List<String> parents = vertex.getParents();
        List<Association> incomingAssociations = vertex.getIncomingAssociations();
        List<Association> outgoingAssociations = vertex.getOutgoingAssociations();

        for (String child : children) {
            graph.get(child).removeAssignment(child, name);
        }

        for (String parent : parents) {
            graph.get(parent).removeAssignment(name, parent);
        }

        for (Association association : incomingAssociations) {
            graph.get(association.getSource()).removeAssociation(association.getSource(), association.getTarget());
        }

        for (Association association : outgoingAssociations) {
            graph.get(association.getTarget()).removeAssociation(association.getSource(), association.getTarget());
        }

        graph.remove(name);
        pcs.remove(name);
    }

    public boolean containsNode(String node) {
        return graph.containsKey(node);
    }

    public void addAssignmentEdge(String source, String target) {
        if (graph.get(source).getParents().contains(target)) {
            return;
        }

        graph.get(source).addAssignment(source, target);
        graph.get(target).addAssignment(source, target);
    }

    public void addAssociationEdge(String source, String target, AccessRightSet set) {
        graph.get(source).addAssociation(source, target, set);
        graph.get(target).addAssociation(source, target, set);
    }

    public void deassign(String child, String parent) {
        graph.get(child).removeAssignment(child, parent);
        graph.get(parent).removeAssignment(child, parent);
    }

    public void dissociate(String ua, String target) {
        graph.get(ua).removeAssociation(ua, target);
        graph.get(target).removeAssociation(ua, target);
    }

    public List<String> getChildren(String node) {
        return new ArrayList<>(graph.get(node).getChildren());
    }

    public List<String> getParents(String node) {
        return new ArrayList<>(graph.get(node).getParents());
    }

    public List<Association> getIncomingAssociations(String node) {
        return new ArrayList<>(graph.get(node).getIncomingAssociations());
    }

    public List<Association> getOutgoingAssociations(String node) {
        return new ArrayList<>(graph.get(node).getOutgoingAssociations());
    }

    public boolean containsEdge(String source, String target) {
        return graph.get(source).getParents().contains(target)
                || graph.get(source).getOutgoingAssociations().contains(new Association(source, target));
    }

    public List<Node> getNodes() {
        Collection<Vertex> values = graph.values();
        List<Node> nodes = new ArrayList<>();
        for (Vertex v : values) {
            nodes.add(v.getNode());
        }
        return nodes;
    }
}
