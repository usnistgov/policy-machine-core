package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.Association;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.HashMap;
import java.util.Map;

class VertexLeaf extends Vertex {

    private ObjectOpenHashSet<String> descendants;

    public VertexLeaf(String name, NodeType type) {
        super(name, type);
        this.descendants = new ObjectOpenHashSet<>();
    }

    @Override
    protected Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    protected ObjectOpenHashSet<String> getAdjacentDescendants() {
        return new ObjectOpenHashSet<>(descendants);
    }

    @Override
    protected ObjectOpenHashSet<String> getAdjacentAscendants() {
        return new ObjectOpenHashSet<>();
    }

    @Override
    protected ObjectOpenHashSet<Association> getOutgoingAssociations() {
        return new ObjectOpenHashSet<>();
    }

    @Override
    protected ObjectOpenHashSet<Association> getIncomingAssociations() {
        return new ObjectOpenHashSet<>();
    }

    @Override
    public void addAssignment(String ascendant, String descendant) {
        descendants.add(descendant);
    }

    @Override
    public void deleteAssignment(String ascendant, String descendant) {
        descendants.remove(descendant);
    }

    @Override
    public void addAssociation(String ua, String target, AccessRightSet accessRightSet) {

    }

    @Override
    public void deleteAssociation(String ua, String target) {

    }
}
