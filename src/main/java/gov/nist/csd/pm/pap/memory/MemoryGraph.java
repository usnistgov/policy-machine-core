package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.Graph;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.io.Serializable;
import java.util.*;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.WILDCARD;

class MemoryGraph implements Graph {

    private final Map<String, Vertex> graph;
    private final AccessRightSet resourceAccessRights;
    private final List<String> pcs;
    private final List<String> oas;
    private final List<String> uas;
    private final List<String> os;
    private final List<String> us;

    protected MemoryTx tx;

    public MemoryGraph() {
        this.graph = new HashMap<>();
        this.pcs = new ArrayList<>();
        this.oas = new ArrayList<>();
        this.uas = new ArrayList<>();
        this.os = new ArrayList<>();
        this.us = new ArrayList<>();
        this.resourceAccessRights = new AccessRightSet();
        tx = new MemoryTx(false, 0, null);
    }

    public MemoryGraph(MemoryGraph graph) {
        this.graph = new HashMap<>();
        for (String n : graph.graph.keySet()) {
            this.graph.put(n, graph.graph.get(n).copy());
        }

        this.pcs = new ArrayList<>(graph.pcs);
        this.oas = new ArrayList<>(graph.oas);
        this.uas = new ArrayList<>(graph.uas);
        this.os = new ArrayList<>(graph.os);
        this.us = new ArrayList<>(graph.us);
        this.resourceAccessRights = new AccessRightSet(graph.getResourceAccessRights());
        this.tx = new MemoryTx(false, 0, null);
    }

    public MemoryGraph(Graph graph) throws PMException {
        this.graph = new HashMap<>();
        this.pcs = new ArrayList<>();
        this.oas = new ArrayList<>();
        this.uas = new ArrayList<>();
        this.os = new ArrayList<>();
        this.us = new ArrayList<>();
        this.resourceAccessRights = new AccessRightSet(graph.getResourceAccessRights());
        this.tx = new MemoryTx(false, 0, null);

        List<String> nodes = graph.search(ANY, NO_PROPERTIES);
        
        // add nodes to graph
        List<String> uas = new ArrayList<>();
        for (String n : nodes) {
            Node node = graph.getNode(n);
            addNode(n, node.getType(), node.getProperties());
            
            if (node.getType() == UA) {
                uas.add(n);
            }
        }

        // add assignments to graph
        for (String n : nodes) {
            List<String> parents = graph.getParents(n);
            for (String p : parents) {
                assignInternal(n, p);
            }
        }
        
        // add associations to graph
        for (String ua : uas) {
            List<Association> assocs = graph.getAssociationsWithSource(ua);
            for (Association a : assocs) {
                associate(ua, a.getTarget(), a.getAccessRightSet());
            }
        }
    }

    public void clear() {
        graph.clear();
        resourceAccessRights.clear();
        pcs.clear();
        oas.clear();
        uas.clear();
        us.clear();
        os.clear();
    }

    @Override
    public void setResourceAccessRights(AccessRightSet accessRightSet) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().graph().setResourceAccessRights(accessRightSet);
        }

        this.resourceAccessRights.clear();
        this.resourceAccessRights.addAll(accessRightSet);
    }

    @Override
    public AccessRightSet getResourceAccessRights() {
        return new AccessRightSet(resourceAccessRights);
    }

    @Override
    public String createPolicyClass(String name, Map<String, String> properties) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().graph().createPolicyClass(name, properties);
        }

        this.graph.put(name, getVertex(name, PC, properties));
        this.pcs.add(name);

        return name;
    }

    @Override
    public String createPolicyClass(String name) throws PMException {
        return createPolicyClass(name, NO_PROPERTIES);
    }

    @Override
    public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().graph().createUserAttribute(name, properties, parent, parents);
        }

        return addNode(name, UA, properties, parent, parents);
    }

    @Override
    public String createUserAttribute(String name, String parent, String... parents) throws PMException {
        return createUserAttribute(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().graph().createObjectAttribute(name, properties, parent, parents);
        }

        return addNode(name, OA, properties, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, String parent, String... parents) throws PMException {
        return createObjectAttribute(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createObject(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().graph().createObject(name, properties, parent, parents);
        }

        return addNode(name, O, properties, parent, parents);
    }

    @Override
    public String createObject(String name, String parent, String... parents) throws PMException {
        return createObject(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createUser(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().graph().createUser(name, properties, parent, parents);
        }

        return addNode(name, U, properties, parent, parents);
    }

    @Override
    public String createUser(String name, String parent, String... parents) throws PMException {
        return createUser(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> properties) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().graph().setNodeProperties(name, properties);
        }

        this.graph.get(name).setProperties(properties);
    }

    @Override
    public boolean nodeExists(String name) {
        return this.graph.containsKey(name);
    }


    @Override
    public Node getNode(String name) {
        return new Node(this.graph.get(name).getNode());
    }

    @Override
    public List<String> search(NodeType type, Map<String, String> properties) {
        List<String> nodes = filterByType(type);
        return filterByProperties(nodes, properties);
    }

    @Override
    public List<String> getPolicyClasses() {
        return new ArrayList<>(pcs);
    }

    @Override
    public void deleteNode(String name) throws PMException {
        if (!graph.containsKey(name)) {
            return;
        }

        if (tx.isActive()) {
            tx.getPolicyStore().graph().deleteNode(name);
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

        if (vertex.getNode().getType() == PC) {
            pcs.remove(name);
        } else if (vertex.getNode().getType() == OA) {
            oas.remove(name);
        } else if (vertex.getNode().getType() == UA) {
            uas.remove(name);
        } else if (vertex.getNode().getType() == O) {
            os.remove(name);
        } else if (vertex.getNode().getType() == U) {
            us.remove(name);
        }
    }

    @Override
    public void assign(String child, String parent) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().graph().assign(child, parent);
        }

        assignInternal(child, parent);
    }

    @Override
    public void deassign(String child, String parent) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().graph().deassign(child, parent);
        }

        deassignInternal(child, parent);
    }

    @Override
    public void assignAll(List<String> children, String target) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().graph().assignAll(children, target);
        }

        for (String c : children) {
            assignInternal(c, target);
        }
    }

    @Override
    public void deassignAll(List<String> children, String target) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().graph().deassignAll(children, target);
        }

        for (String c : children) {
            deassignInternal(c, target);
        }
    }

    @Override
    public void deassignAllFromAndDelete(String target) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().graph().deassignAllFromAndDelete(target);
        }

        for (String c : getChildren(target)) {
            deassignInternal(c, target);
        }

        deleteNode(target);
    }

    @Override
    public List<String> getParents(String node) {
        return new ArrayList<>(graph.get(node).getParents());
    }


    @Override
    public List<String> getChildren(String node) {
        return new ArrayList<>(graph.get(node).getChildren());
    }


    @Override
    public void associate(String ua, String target, AccessRightSet accessRights) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().graph().associate(ua, target, accessRights);
        }

        if (containsEdge(ua, target)) {
            // remove the existing association edge in order to update it
            dissociateInternal(ua, target);
        }

        associateInternal(ua, target, accessRights);
    }

    @Override
    public void dissociate(String ua, String target) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().graph().dissociate(ua, target);
        }

        dissociateInternal(ua, target);
    }

    @Override
    public List<Association> getAssociationsWithSource(String ua) {
        return new ArrayList<>(graph.get(ua).getOutgoingAssociations());
    }

    @Override
    public List<Association> getAssociationsWithTarget(String target) {
        return new ArrayList<>(graph.get(target).getIncomingAssociations());
    }

    public String addNode(String name, NodeType type, Map<String, String> properties) {
        this.graph.put(name, getVertex(name, type, properties));
        if (type == NodeType.PC) {
            this.pcs.add(name);
        } else if (type == OA){
            this.oas.add(name);
        } else if (type == UA){
            this.uas.add(name);
        } else if (type == O){
            this.os.add(name);
        } else if (type == U){
            this.us.add(name);
        }

        return name;
    }

    private String addNode(String name, NodeType type, Map<String, String> properties, String initialParent, String ... parents) throws PMException {
        addNode(name, type, properties);

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

    private List<String> filterByProperties(List<String> nodes, Map<String, String> properties) {
        List<String> results = new ArrayList<>();
        if (properties.isEmpty()) {
            results.addAll(nodes);
        } else {
            for (String n : nodes) {
                Map<String, String> nodeProperties = graph.get(n).getNode().getProperties();

                if (!hasAllKeys(nodeProperties, properties)
                        || !valuesMatch(nodeProperties, properties)) {
                    continue;
                }

                results.add(n);
            }
        }

        return results;
    }

    private List<String> filterByType(NodeType type) {
        List<String> nodes = new ArrayList<>();
        if (type != ANY) {
            if (type == PC) {
                nodes.addAll(pcs);
            } else if (type == OA) {
                nodes.addAll(oas);
            } else if (type == UA) {
                nodes.addAll(uas);
            } else if (type == O) {
                nodes.addAll(os);
            } else {
                nodes.addAll(us);
            }
        } else {
            nodes.addAll(pcs);
            nodes.addAll(uas);
            nodes.addAll(oas);
            nodes.addAll(us);
            nodes.addAll(os);
        }

        return nodes;
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

    public void assignInternal(String child, String parent) {
        if (graph.get(child).getParents().contains(parent)) {
            return;
        }

        graph.get(child).addAssignment(child, parent);
        graph.get(parent).addAssignment(child, parent);
    }

    public void deassignInternal(String child, String parent) {
        graph.get(child).removeAssignment(child, parent);
        graph.get(parent).removeAssignment(child, parent);
    }

    public void associateInternal(String ua, String target, AccessRightSet accessRights) {
        graph.get(ua).addAssociation(ua, target, accessRights);
        graph.get(target).addAssociation(ua, target, accessRights);
    }

    public void dissociateInternal(String ua, String target) {
        graph.get(ua).removeAssociation(ua, target);
        graph.get(target).removeAssociation(ua, target);
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
