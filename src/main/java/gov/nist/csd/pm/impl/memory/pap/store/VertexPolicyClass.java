package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.node.NodeType;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.HashMap;
import java.util.Map;

class VertexPolicyClass extends Vertex {
    private ObjectOpenHashSet<String> ascendants;

    public VertexPolicyClass(String name) {
        super(name, NodeType.PC);
        this.ascendants = new ObjectOpenHashSet<>();
    }

    public VertexPolicyClass(String name, ObjectOpenHashSet<String> ascendants) {
        super(name, NodeType.PC);
        this.ascendants = ascendants;
    }

    @Override
    protected Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    protected long[] getAdjacentDescendants() {
        return new ObjectOpenHashSet<>();
    }

    @Override
    protected long[] getAdjacentAscendants() {
        return new ObjectOpenHashSet<>(ascendants);
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
        ascendants.add(ascendant);
    }

    @Override
    public void deleteAssignment(long ascendant, long descendant) {
        ascendants.remove(ascendant);
    }

    @Override
    public void addAssociation(long ua, long target, AccessRightSet accessRightSet) {

    }

    @Override
    public void deleteAssociation(long ua, long target) {

    }
}
