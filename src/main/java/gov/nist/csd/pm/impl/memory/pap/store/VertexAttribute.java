package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.Association;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.HashMap;
import java.util.Map;

class VertexAttribute extends Vertex {

    private ObjectOpenHashSet<String> descendants;
    private ObjectOpenHashSet<String> ascendants;
    private ObjectOpenHashSet<Association> outgoingAssociations;
    private ObjectOpenHashSet<Association> incomingAssociations;

    public VertexAttribute(String name, NodeType type) {
        super(name, type);
        this.descendants = new ObjectOpenHashSet<>();
        this.ascendants = new ObjectOpenHashSet<>();
        this.outgoingAssociations = new ObjectOpenHashSet<>();
        this.incomingAssociations = new ObjectOpenHashSet<>();
    }

    @Override
    protected Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    public ObjectOpenHashSet<String> getAdjacentDescendants() {
        return new ObjectOpenHashSet<>(descendants);
    }

    @Override
    public ObjectOpenHashSet<String> getAdjacentAscendants() {
        return new ObjectOpenHashSet<>(ascendants);
    }

    @Override
    public ObjectOpenHashSet<Association> getOutgoingAssociations() {
        return new ObjectOpenHashSet<>(outgoingAssociations);
    }

    @Override
    public ObjectOpenHashSet<Association> getIncomingAssociations() {
        return new ObjectOpenHashSet<>(incomingAssociations);
    }

    @Override
    protected void addAssignment(String ascendant, String descendant) {
        if (ascendant.equals(name)) {
            descendants.add(descendant);
        } else {
            ascendants.add(ascendant);
        }
    }

    @Override
    protected void deleteAssignment(String ascendant, String descendant) {
        if (ascendant.equals(name)) {
            descendants.remove(descendant);
        } else {
            ascendants.remove(ascendant);
        }
    }

    @Override
    public void addAssociation(String ua, String target, AccessRightSet accessRightSet) {
        if (ua.equals(name)) {
            outgoingAssociations.add(new Association(ua, target, accessRightSet));
        } else {
            incomingAssociations.add(new Association(ua, target, accessRightSet));
        }
    }

    @Override
    public void deleteAssociation(String ua, String target) {
        if (ua.equals(name)) {
            outgoingAssociations.removeIf(a -> a.getSource().equals(ua) && a.getTarget().equals(target));
        } else {
            incomingAssociations.removeIf(a -> a.getSource().equals(ua) && a.getTarget().equals(target));
        }
    }
}

