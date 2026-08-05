package gov.nist.ngac.pm.core.impl.memory.pap.store;

import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.pap.graph.Association;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Collection;
import java.util.Map;

/**
 * A single node in the in-memory graph, tracking its own adjacent assignments and associations.
 */
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

    /**
     * Records this vertex as adjacent-descendant of the given ascendant.
     */
    protected abstract void addAssignment(long ascendant, long descendant);

    /**
     * Removes a previously recorded assignment between this vertex and the given ascendant/descendant.
     */
    protected abstract void deleteAssignment(long ascendant, long descendant);

    /**
     * Records an association from this vertex, as the user attribute, to the given target.
     */
    protected abstract void addAssociation(long ua, long target, AccessRightSet accessRightSet);

    /**
     * Removes a previously recorded association between this vertex and the given user attribute/target.
     */
    protected abstract void deleteAssociation(long ua, long target);

}
