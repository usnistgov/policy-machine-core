package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;

import java.util.Collection;
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
    protected Collection<String> getAdjacentDescendants() {
        return vertex.getAdjacentDescendants();
    }

    @Override
    protected Collection<String> getAdjacentAscendants() {
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
    protected void addAssignment(String ascendant, String descendant) {
        vertex.addAssignment(ascendant, descendant);
    }

    @Override
    protected void deleteAssignment(String ascendant, String descendant) {
        vertex.deleteAssignment(ascendant, descendant);
    }

    @Override
    protected void addAssociation(String ua, String target, AccessRightSet accessRightSet) {
        vertex.addAssociation(ua, target, accessRightSet);
    }

    @Override
    protected void deleteAssociation(String ua, String target) {
        vertex.deleteAssociation(ua, target);
    }
}
