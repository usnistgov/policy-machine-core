package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class VertexObject implements Vertex {

    private final Node node;
    private final List<String> parents;

    public VertexObject(String name, Map<String, String> properties) {
        this.node = new Node(name, NodeType.O, properties);
        this.parents = new ArrayList<>();
    }

    private VertexObject(Node node, List<String> parents) {
        this.node = new Node(node);
        this.parents = new ArrayList<>(parents);
    }

    @Override
    public Vertex copy() {
        return new VertexObject(node, parents);
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
        return Collections.emptyList();
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
        parents.add(parent);
    }

    @Override
    public void deleteAssignment(String child, String parent) {
        parents.remove(parent);
    }

    @Override
    public void addAssociation(String ua, String target, AccessRightSet accessRightSet) {

    }

    @Override
    public void deleteAssociation(String ua, String target) {

    }
}
