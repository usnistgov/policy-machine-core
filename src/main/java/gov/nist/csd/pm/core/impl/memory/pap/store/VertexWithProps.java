package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.graph.relationship.Association;
import java.util.Collection;
import java.util.Map;

public class VertexWithProps extends Vertex{

    private final Vertex vertex;
    private final Map<String, String> properties;

    public VertexWithProps(long id, Vertex vertex, Map<String, String> properties) {
        super(id, vertex.name, vertex.type);
        this.vertex = vertex;
        this.properties = properties;
    }

    public Vertex getVertex() {
        return vertex;
    }

    @Override
    protected Map<String, String> getProperties() {
        return properties;
    }

    @Override
    protected Collection<Long> getAdjacentDescendants() {
        return vertex.getAdjacentDescendants();
    }

    @Override
    protected Collection<Long> getAdjacentAscendants() {
        return vertex.getAdjacentAscendants();
    }

    @Override
    protected Collection<Association> getOutgoingAssociations() {
        return vertex.getOutgoingAssociations();
    }

    @Override
    protected Collection<Association> getIncomingAssociations() {
        return vertex.getIncomingAssociations();
    }

    @Override
    protected void addAssignment(long ascendant, long descendant) {
        vertex.addAssignment(ascendant, descendant);
    }

    @Override
    protected void deleteAssignment(long ascendant, long descendant) {
        vertex.deleteAssignment(ascendant, descendant);
    }

    @Override
    protected void addAssociation(long ua, long target, AccessRightSet accessRightSet) {
        vertex.addAssociation(ua, target, accessRightSet);
    }

    @Override
    protected void deleteAssociation(long ua, long target) {
        vertex.deleteAssociation(ua, target);
    }
}
