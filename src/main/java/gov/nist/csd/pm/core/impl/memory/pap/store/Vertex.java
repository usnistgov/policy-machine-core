package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.graph.relationship.Association;
import java.util.Collection;
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

    protected abstract Collection<Long> getAdjacentDescendants();
    protected abstract Collection<Long> getAdjacentAscendants();
    protected abstract Collection<Association> getOutgoingAssociations();
    protected abstract Collection<Association> getIncomingAssociations();

    protected abstract void addAssignment(long ascendant, long descendant);
    protected abstract void deleteAssignment(long ascendant, long descendant);
    protected abstract void addAssociation(long ua, long target, AccessRightSet accessRightSet);
    protected abstract void deleteAssociation(long ua, long target);

}
