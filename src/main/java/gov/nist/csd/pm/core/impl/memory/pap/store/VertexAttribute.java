package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.graph.relationship.Association;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class VertexAttribute extends Vertex {

    private final LongArrayList descendants;
    private final LongArrayList ascendants;
    private final ObjectArrayList<Association> outgoingAssociations;
    private final ObjectArrayList<Association> incomingAssociations;

    public VertexAttribute(long id, String name, NodeType type) {
        super(id, name, type);
        this.descendants = new LongArrayList();
        this.ascendants = new LongArrayList();
        this.outgoingAssociations = new ObjectArrayList<>();
        this.incomingAssociations = new ObjectArrayList<>();
    }

    @Override
    protected Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    public Collection<Long> getAdjacentDescendants() {
        return descendants;
    }

    @Override
    public Collection<Long> getAdjacentAscendants() {
        return ascendants;
    }

    @Override
    public Collection<Association> getOutgoingAssociations() {
        return outgoingAssociations;
    }

    @Override
    public Collection<Association> getIncomingAssociations() {
        return incomingAssociations;
    }

    @Override
    protected void addAssignment(long ascendant, long descendant) {
        if (ascendant == id) {
            descendants.add(descendant);
        } else {
            ascendants.add(ascendant);
        }
    }

    @Override
    protected void deleteAssignment(long ascendant, long descendant) {
        if (ascendant == id) {
            descendants.removeLong(descendants.indexOf(descendant));
        } else {
            ascendants.removeLong(ascendants.indexOf(ascendant));
        }
    }

    @Override
    public void addAssociation(long ua, long target, AccessRightSet accessRightSet) {
        if (ua == id) {
            outgoingAssociations.add(new Association(ua, target, accessRightSet));
        } else {
            incomingAssociations.add(new Association(ua, target, accessRightSet));
        }
    }

    @Override
    public void deleteAssociation(long ua, long target) {
        if (ua == id) {
            outgoingAssociations.removeIf(a -> a.getSource() == ua && a.getTarget() == target);
        } else {
            incomingAssociations.removeIf(a -> a.getSource() == ua && a.getTarget() == target);
        }
    }
}

