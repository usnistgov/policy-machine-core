package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;

import java.util.Collection;
import java.util.Map;

public abstract class Vertex {

    protected String name;
    protected NodeType type;

    public Vertex(String name, NodeType type) {
        this.name = name;
        this.type = type;
    }

    protected String getName() {
        return name;
    }

    protected NodeType getType() {
        return type;
    }

    protected abstract Map<String, String> getProperties();

    protected abstract Collection<String> getAdjacentDescendants();
    protected abstract Collection<String> getAdjacentAscendants();
    protected abstract Collection<Association> getOutgoingAssociations();
    protected abstract Collection<Association> getIncomingAssociations();

    protected abstract void addAssignment(String ascendant, String descendant);
    protected abstract void deleteAssignment(String ascendant, String descendant);
    protected abstract void addAssociation(String ua, String target, AccessRightSet accessRightSet);
    protected abstract void deleteAssociation(String ua, String target);

}
