package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.Association;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

class VertexPolicyClass extends Vertex {
    private long[] ascendants;

    public VertexPolicyClass(long id, String name) {
        super(id, name, NodeType.PC);
        this.ascendants = new long[0];
    }

    public VertexPolicyClass(long id, String name, long[] ascendants) {
        super(id, name, NodeType.PC);
        this.ascendants = ascendants;
    }

    @Override
    protected Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    protected long[] getAdjacentDescendants() {
        return new long[]{};
    }

    @Override
    protected long[] getAdjacentAscendants() {
        return ascendants;
    }

    @Override
    protected Association[] getOutgoingAssociations() {
        return new Association[]{};
    }

    @Override
    protected Association[] getIncomingAssociations() {
        return new Association[]{};
    }

    @Override
    public void addAssignment(long ascendant, long descendant) {
        ascendants = Arrays.copyOf(ascendants, ascendants.length + 1);
        ascendants[ascendants.length-1] = ascendant;
    }

    @Override
    public void deleteAssignment(long ascendant, long descendant) {
        ascendants = LongStream.of(ascendants)
                .filter(asc -> asc != ascendant)
                .toArray();
    }

    @Override
    public void addAssociation(long ua, long target, AccessRightSet accessRightSet) {

    }

    @Override
    public void deleteAssociation(long ua, long target) {

    }
}
