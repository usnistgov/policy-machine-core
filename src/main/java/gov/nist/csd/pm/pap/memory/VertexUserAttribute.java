package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class VertexUserAttribute implements Vertex {

    private final Node node;
    private final List<String> parents;
    private final List<String> children;
    private final List<Association> outgoingAssociations;
    private final List<Association> incomingAssociations;

    public VertexUserAttribute(String name, Map<String, String> properties) {
        this.node = new Node(name, NodeType.UA, properties);
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        this.outgoingAssociations = new ArrayList<>();
        this.incomingAssociations = new ArrayList<>();
    }

    private VertexUserAttribute(Node node, List<String> parents, List<String> children, List<Association> outgoing, List<Association> incoming) {
        this.node = new Node(node);
        this.parents = new ArrayList<>(parents);
        this.children = new ArrayList<>(children);
        this.outgoingAssociations = new ArrayList<>(outgoing);
        this.incomingAssociations = new ArrayList<>(incoming);
    }

    @Override
    public Vertex copy() {
        return new VertexUserAttribute(node, parents, children, outgoingAssociations, incomingAssociations);
    }

    @Override
    public void setProperties(Map<String, String> properties) {
        node.setProperties(properties);
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public List<String> getParents() {
        return parents;
    }

    @Override
    public List<String> getChildren() {
        return children;
    }

    @Override
    public List<Association> getOutgoingAssociations() {
        return outgoingAssociations;
    }

    @Override
    public List<Association> getIncomingAssociations() {
        return incomingAssociations;
    }

    @Override
    public void addAssignment(String child, String parent) {
        if (child.equals(node.getName())) {
            parents.add(parent);
        } else {
            children.add(child);
        }
    }

    @Override
    public void deleteAssignment(String child, String parent) {
        if (child.equals(node.getName())) {
            parents.remove(parent);
        } else {
            children.remove(child);
        }
    }

    @Override
    public void addAssociation(String ua, String target, AccessRightSet accessRightSet) {
        if (ua.equals(node.getName())) {
            outgoingAssociations.add(new Association(ua, target, accessRightSet));
        } else {
            incomingAssociations.add(new Association(ua, target, accessRightSet));
        }
    }

    @Override
    public void deleteAssociation(String ua, String target) {
        if (ua.equals(node.getName())) {
            outgoingAssociations.remove(new Association(ua, target));
        } else {
            incomingAssociations.remove(new Association(ua, target));
        }
    }
}
