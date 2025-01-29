package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.Association;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

class VertexLeaf extends Vertex {

    private long[] descendants;

    public VertexLeaf(long id, String name, NodeType type) {
        super(id, name, type);
        this.descendants = new long[]{};
    }

    @Override
    protected Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    protected long[] getAdjacentDescendants() {
        return descendants;
    }

    @Override
    protected long[] getAdjacentAscendants() {
        return new long[]{};
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
        descendants = Arrays.copyOf(descendants, descendants.length + 1);
        descendants[descendants.length - 1] = descendant;
    }

    @Override
    public void deleteAssignment(long ascendant, long descendant) {
        descendants = LongStream.of(descendants)
                .filter(desc -> desc == descendant)
                .toArray();
    }

    @Override
    public void addAssociation(long ua, long target, AccessRightSet accessRightSet) {

    }

    @Override
    public void deleteAssociation(long ua, long target) {

    }
}
