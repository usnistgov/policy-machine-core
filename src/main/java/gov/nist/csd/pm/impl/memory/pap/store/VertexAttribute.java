package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.Association;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;
import java.util.stream.Stream;

class VertexAttribute extends Vertex {

    private long[] descendants;
    private long[] ascendants;
    private Association[] outgoingAssociations;
    private Association[] incomingAssociations;

    public VertexAttribute(long id, String name, NodeType type) {
        super(id, name, type);
        this.descendants = new long[0];
        this.ascendants = new long[0];
        this.outgoingAssociations = new Association[0];
        this.incomingAssociations = new Association[0];
    }

    @Override
    protected Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    public long[] getAdjacentDescendants() {
        return descendants;
    }

    @Override
    public long[] getAdjacentAscendants() {
        return ascendants;
    }

    @Override
    public Association[] getOutgoingAssociations() {
        return outgoingAssociations;
    }

    @Override
    public Association[] getIncomingAssociations() {
        return incomingAssociations;
    }

    @Override
    protected void addAssignment(long ascendant, long descendant) {
        if (ascendant == id) {
            descendants = Arrays.copyOf(descendants, descendants.length + 1);
            descendants[descendants.length - 1] = descendant;
        } else {
            ascendants = Arrays.copyOf(ascendants, ascendants.length + 1);
            ascendants[ascendants.length - 1] = ascendant;
        }
    }

    @Override
    protected void deleteAssignment(long ascendant, long descendant) {
        if (ascendant == id) {
            descendants = LongStream.of(descendants)
                    .filter(desc -> desc != descendant)
                    .toArray();
        } else {
            ascendants = LongStream.of(ascendants)
                    .filter(asc -> asc != ascendant)
                    .toArray();
        }
    }

    @Override
    public void addAssociation(long ua, long target, AccessRightSet accessRightSet) {
        if (ua == id) {
            outgoingAssociations = Arrays.copyOf(outgoingAssociations, outgoingAssociations.length + 1);
            outgoingAssociations[outgoingAssociations.length-1] = new Association(ua, target, accessRightSet);
        } else {
            incomingAssociations = Arrays.copyOf(incomingAssociations, incomingAssociations.length + 1);
            incomingAssociations[incomingAssociations.length-1] = new Association(ua, target, accessRightSet);
        }
    }

    @Override
    public void deleteAssociation(long ua, long target) {
        if (ua == id) {
            outgoingAssociations = Stream.of(outgoingAssociations)
                    .filter(a -> !(a.getSource() == ua && a.getTarget() == target))
                    .toArray(Association[]::new);
        } else {
            incomingAssociations = Stream.of(incomingAssociations)
                    .filter(a -> !(a.getSource() == ua && a.getTarget() == target))
                    .toArray(Association[]::new);
        }
    }
}

