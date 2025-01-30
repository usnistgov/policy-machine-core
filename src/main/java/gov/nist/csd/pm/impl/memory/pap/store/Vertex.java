package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;

import java.util.Map;

public abstract class Vertex {

    protected long id;
    protected String name;
    protected NodeType type;

    public Vertex(long id, String name, NodeType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    protected long getId() {
        return id;
    }

    protected String getName() {
        return name;
    }

    protected NodeType getType() {
        return type;
    }

    protected abstract Map<String, String> getProperties();

    protected abstract long[] getAdjacentDescendants();
    protected abstract long[] getAdjacentAscendants();
    protected abstract Association[] getOutgoingAssociations();
    protected abstract Association[] getIncomingAssociations();

    protected abstract void addAssignment(long ascendant, long descendant);
    protected abstract void deleteAssignment(long ascendant, long descendant);
    protected abstract void addAssociation(long ua, long target, AccessRightSet accessRightSet);
    protected abstract void deleteAssociation(long ua, long target);

}
