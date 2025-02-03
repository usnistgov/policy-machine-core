package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class VertexPolicyClass extends Vertex {
    private final LongArrayList ascendants;

    public VertexPolicyClass(long id, String name) {
        super(id, name, NodeType.PC);
        this.ascendants = new LongArrayList();
    }

    @Override
    protected Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    protected Collection<Long> getAdjacentDescendants() {
        return new LongArrayList();
    }

    @Override
    protected Collection<Long> getAdjacentAscendants() {
        return ascendants;
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
        ascendants.add(ascendant);
    }

    @Override
    public void deleteAssignment(long ascendant, long descendant) {
        ascendants.removeLong(ascendants.indexOf(ascendant));
    }

    @Override
    public void addAssociation(long ua, long target, AccessRightSet accessRightSet) {

    }

    @Override
    public void deleteAssociation(long ua, long target) {

    }
}
