package gov.nist.ngac.pm.core.impl.memory.pap.store;

import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.pap.graph.Association;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Collection;
import java.util.Map;

/**
 * A node in the in-memory graph, tracking its own assignments and associations.
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
     * Creates an assignment between the two nodes.
     */
    protected abstract void addAssignment(long ascendant, long descendant);

    /**
     * Deletes an assignment between the two nodes.
     */
    protected abstract void deleteAssignment(long ascendant, long descendant);

    /**
     * Adds an association between the two node with the given access rights
     */
    protected abstract void addAssociation(long ua, long target, AccessRightSet accessRightSet);

    /**
     * Removes a previously recorded association between this vertex and the given target.
     */
    protected abstract void deleteAssociation(long ua, long target);

}
