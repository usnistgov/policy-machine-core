package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.graph.node.NodeType;
import gov.nist.csd.pm.pap.graph.relationship.Association;
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
    protected ObjectOpenHashSet<String> getAdjacentDescendants() {
        return new ObjectOpenHashSet<>();
    }

    @Override
    protected ObjectOpenHashSet<String> getAdjacentAscendants() {
        return new ObjectOpenHashSet<>(ascendants);
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
        ascendants.add(ascendant);
    }

    @Override
    public void deleteAssignment(String ascendant, String descendant) {
        ascendants.remove(ascendant);
    }

    @Override
    public void addAssociation(String ua, String target, AccessRightSet accessRightSet) {

    }

    @Override
    public void deleteAssociation(String ua, String target) {

    }
}
