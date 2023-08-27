package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class VertexPolicyClass implements Vertex {

    private final Node node;
    private final List<String> children;

    public VertexPolicyClass(String name, Map<String, String> properties) {
        this.node = new Node(name, NodeType.PC, properties);
        this.children = new ArrayList<>();
    }

    private VertexPolicyClass(Node node, List<String> children) {
        this.node = new Node(node);
        this.children = new ArrayList<>(children);
    }

    @Override
    public Vertex copy() {
        return new VertexPolicyClass(node, children);
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
        return Collections.emptyList();
    }

    @Override
    public List<String> getChildren() {
        return children;
    }

    @Override
    public List<Association> getOutgoingAssociations() {
        return Collections.emptyList();
    }

    @Override
    public List<Association> getIncomingAssociations() {
        return Collections.emptyList();
    }

    @Override
    public void addAssignment(String child, String parent) {
        children.add(child);
    }

    @Override
    public void deleteAssignment(String child, String parent) {
        children.remove(child);
    }

    @Override
    public void addAssociation(String ua, String target, AccessRightSet accessRightSet) {

    }

    @Override
    public void deleteAssociation(String ua, String target) {

    }
}
