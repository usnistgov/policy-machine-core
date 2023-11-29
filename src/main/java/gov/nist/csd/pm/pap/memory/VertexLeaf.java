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

class VertexLeaf extends Vertex {

    private UnmodifiableNode node;
    private List<String> parents;

    VertexLeaf(String name, NodeType type, Map<String, String> properties) {
        this.node = new UnmodifiableNode(name, type, properties);
        this.parents = Collections.unmodifiableList(new ArrayList<>());
    }

    @Override
    protected void setProperties(Map<String, String> properties) {
        node = new UnmodifiableNode(node.getName(), node.getType(), properties);
    }

    @Override
    protected UnmodifiableNode getNode() {
        return node;
    }

    @Override
    protected List<String> getParents() {
        return parents;
    }

    @Override
    protected List<String> getChildren() {
        return Collections.emptyList();
    }

    @Override
    protected List<Association> getOutgoingAssociations() {
        return Collections.emptyList();
    }

    @Override
    protected List<Association> getIncomingAssociations() {
        return Collections.emptyList();
    }

    @Override
    public void addAssignment(String child, String parent) {
        List<String> l = new ArrayList<>(parents);
        l.add(parent);
        parents = Collections.unmodifiableList(l);
    }

    @Override
    public void deleteAssignment(String child, String parent) {
        List<String> l = new ArrayList<>(parents);
        l.remove(parent);
        parents = Collections.unmodifiableList(l);
    }

    @Override
    public void addAssociation(String ua, String target, AccessRightSet accessRightSet) {

    }

    @Override
    public void deleteAssociation(String ua, String target) {

    }
}
