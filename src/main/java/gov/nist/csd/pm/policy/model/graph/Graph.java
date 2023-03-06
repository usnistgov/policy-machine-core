package gov.nist.csd.pm.policy.model.graph;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.*;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.WILDCARD;

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

    public String addNode(String name, NodeType type, Map<String, String> properties) {
        this.graph.put(name, getVertex(name, type, properties));
        if (type == NodeType.PC) {
            this.pcs.add(name);
        }

        return name;
    }

    public AccessRightSet getResourceAccessRights() {
        return resourceAccessRights;
    }

    public boolean nodeExists(String name) {
        return graph.containsKey(name);
    }

    public void setResourceAccessRights(AccessRightSet resourceAccessRights) {
        this.resourceAccessRights.clear();
        this.resourceAccessRights.addAll(resourceAccessRights);
    }

    public String createPolicyClass(String name, Map<String, String> properties) {
        this.graph.put(name, getVertex(name, PC, properties));
        this.pcs.add(name);

        return name;
    }

    public String createPolicyClass(String name) {
        return createPolicyClass(name, new HashMap<>());
    }

    public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        return addNode(name, UA, properties, parent, parents);
    }

    public String createUserAttribute(String name, String parent, String... parents) {
        return createUserAttribute(name, new HashMap<>(), parent, parents);
    }

    public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        return addNode(name, OA, properties, parent, parents);
    }

    public String createObjectAttribute(String name, String parent, String... parents) {
        return createObjectAttribute(name, new HashMap<>(), parent, parents);
    }

    public String createObject(String name, Map<String, String> properties, String parent, String... parents) {
        return addNode(name, O, properties, parent, parents);
    }

    public String createObject(String name, String parent, String... parents) {
        return createObject(name, new HashMap<>(), parent, parents);
    }

    public String createUser(String name, Map<String, String> properties, String parent, String... parents) {
        return addNode(name, U, properties, parent, parents);
    }

    public String createUser(String name, String parent, String... parents) {
        return createUser(name, new HashMap<>(), parent, parents);
    }

    private String addNode(String name, NodeType type, Map<String, String> properties, String initialParent, String ... parents) {
        this.graph.put(name, getVertex(name, type, properties));
        assign(name, initialParent);
        for (String parent : parents) {
            assign(name, parent);
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

    public List<String> search(NodeType type, Map<String, String> checkProperties) {
        List<String> results = new ArrayList<>();
        // iterate over the nodes to find ones that match the search parameters
        for (String name : graph.keySet()) {
            Node node = getNode(name);
            Map<String, String> nodeProperties = node.getProperties();

            // if the type parameter is not null and the current node type does not equal the type parameter, do not add
            if (type != ANY && !node.getType().equals(type)
                    || !hasAllKeys(nodeProperties, checkProperties)
                    || !valuesMatch(nodeProperties, checkProperties)) {
                continue;
            }

            results.add(node.getName());
        }

        return results;
    }

    private boolean valuesMatch(Map<String, String> nodeProperties, Map<String, String> checkProperties) {
        for (Map.Entry<String, String> entry : checkProperties.entrySet()) {
            String checkKey = entry.getKey();
            String checkValue = entry.getValue();
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

    public List<String> getPolicyClasses() {
        return pcs;
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

    public void assign(String child, String parent) {
        if (graph.get(child).getParents().contains(parent)) {
            return;
        }

        graph.get(child).addAssignment(child, parent);
        graph.get(parent).addAssignment(child, parent);
    }

    public void deassign(String child, String parent) {
        graph.get(child).removeAssignment(child, parent);
        graph.get(parent).removeAssignment(child, parent);
    }

    public void associate(String ua, String target, AccessRightSet accessRights) {
        graph.get(ua).addAssociation(ua, target, accessRights);
        graph.get(target).addAssociation(ua, target, accessRights);
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

    public List<Association> getAssociationsWithSource(String ua) {
        return new ArrayList<>(graph.get(ua).getOutgoingAssociations());
    }

    public List<Association> getAssociationsWithTarget(String target) {
        return new ArrayList<>(graph.get(target).getIncomingAssociations());
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
