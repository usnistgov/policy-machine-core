package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;

import java.util.Map;

public class VertexWithProps extends Vertex{

    private Vertex vertex;
    private Map<String, String> properties;

    public VertexWithProps(Vertex vertex, Map<String, String> properties) {
        super(vertex.name, vertex.type);
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
    protected long[] getAdjacentDescendants() {
        return vertex.getAdjacentDescendants();
    }

    @Override
    protected long[] getAdjacentAscendants() {
        return vertex.getAdjacentAscendants();
    }

    @Override
    protected long[] getOutgoingAssociations() {
        return vertex.getOutgoingAssociations();
    }

    @Override
    protected long[] getIncomingAssociations() {
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
