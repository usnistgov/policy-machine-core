package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.memory.unmodifiable.UnmodifiableAssociation;
import gov.nist.csd.pm.pap.memory.unmodifiable.UnmodifiableNode;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class VertexAttribute extends Vertex {

    private UnmodifiableNode node;
    private List<String> parents;
    private List<String> children;
    private List<Association> outgoingAssociations;
    private List<Association> incomingAssociations;

    public VertexAttribute(String name, NodeType type, Map<String, String> properties) {
        this.node = new UnmodifiableNode(name, type, properties);
        this.parents = Collections.unmodifiableList(new ArrayList<>());
        this.children = Collections.unmodifiableList(new ArrayList<>());
        this.outgoingAssociations = Collections.unmodifiableList(new ArrayList<>());
        this.incomingAssociations = Collections.unmodifiableList(new ArrayList<>());
    }

    @Override
    protected void setProperties(Map<String, String> properties) {
        node = new UnmodifiableNode(node.getName(), node.getType(), properties);
    }

    @Override
    protected Node getNode() {
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
    protected void addAssignment(String child, String parent) {
        if (child.equals(node.getName())) {
            List<String> l = new ArrayList<>(parents);
            l.add(parent);
            parents = Collections.unmodifiableList(l);
        } else {
            List<String> l = new ArrayList<>(children);
            l.add(child);
            children = Collections.unmodifiableList(l);
        }
    }

    @Override
    protected void deleteAssignment(String child, String parent) {
        if (child.equals(node.getName())) {
            List<String> l = new ArrayList<>(parents);
            l.remove(parent);
            parents = Collections.unmodifiableList(l);
        } else {
            List<String> l = new ArrayList<>(children);
            l.remove(child);
            children = Collections.unmodifiableList(l);
        }
    }

    @Override
    public void addAssociation(String ua, String target, AccessRightSet accessRightSet) {
        if (ua.equals(node.getName())) {
            List<Association> assocs = new ArrayList<>(outgoingAssociations);
            assocs.add(new UnmodifiableAssociation(ua, target, accessRightSet));

            this.outgoingAssociations = Collections.unmodifiableList(assocs);
        } else {
            List<Association> assocs = new ArrayList<>(incomingAssociations);
            assocs.add(new UnmodifiableAssociation(ua, target, accessRightSet));

            this.incomingAssociations = Collections.unmodifiableList(assocs);
        }
    }

    @Override
    public void deleteAssociation(String ua, String target) {
        if (ua.equals(node.getName())) {
            List<Association> assocs = new ArrayList<>(outgoingAssociations);
            assocs.removeIf(a -> a.getSource().equals(ua) && a.getTarget().equals(target));

            this.outgoingAssociations = Collections.unmodifiableList(assocs);
        } else {
            List<Association> assocs = new ArrayList<>(incomingAssociations);
            assocs.removeIf(a -> a.getSource().equals(ua) && a.getTarget().equals(target));

            this.incomingAssociations = Collections.unmodifiableList(assocs);
        }
    }
}
