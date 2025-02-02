package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.Association;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

class VertexLeaf extends Vertex {

    private LongArrayList descendants;

    public VertexLeaf(long id, String name, NodeType type) {
        super(id, name, type);
        this.descendants = new LongArrayList();
    }

    @Override
    protected Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    protected Collection<Long> getAdjacentDescendants() {
        return descendants;
    }

    @Override
    protected Collection<Long> getAdjacentAscendants() {
        return new LongArrayList();
    }

    @Override
    protected Collection<Association> getOutgoingAssociations() {
        return new ObjectArrayList<>();
    }

    @Override
    protected Collection<Association> getIncomingAssociations() {
        return new ObjectArrayList<>();
    }

    @Override
    public void addAssignment(long ascendant, long descendant) {
        descendants.add(descendant);
    }

    @Override
    public void deleteAssignment(long ascendant, long descendant) {
        descendants.removeLong(descendants.indexOf(descendant));
    }

    @Override
    public void addAssociation(long ua, long target, AccessRightSet accessRightSet) {

    }

    @Override
    public void deleteAssociation(long ua, long target) {

    }
}
