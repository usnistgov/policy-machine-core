package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.node.NodeType;
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
    protected long[] getAdjacentDescendants() {
        return new ObjectOpenHashSet<>(descendants);
    }

    @Override
    protected long[] getAdjacentAscendants() {
        return new ObjectOpenHashSet<>();
    }

    @Override
    protected long[] getOutgoingAssociations() {
        return new ObjectOpenHashSet<>();
    }

    @Override
    protected long[] getIncomingAssociations() {
        return new ObjectOpenHashSet<>();
    }

    @Override
    public void addAssignment(long ascendant, long descendant) {
        descendants.add(descendant);
    }

    @Override
    public void deleteAssignment(long ascendant, long descendant) {
        descendants.remove(descendant);
    }

    @Override
    public void addAssociation(long ua, long target, AccessRightSet accessRightSet) {

    }

    @Override
    public void deleteAssociation(long ua, long target) {

    }
}
