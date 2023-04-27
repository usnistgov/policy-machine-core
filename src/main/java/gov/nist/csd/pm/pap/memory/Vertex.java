package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.List;
import java.util.Map;

interface Vertex {

    Vertex copy();
    void setProperties(Map<String, String> properties);
    Node getNode();
    List<String> getParents();
    List<String> getChildren();
    List<Association> getOutgoingAssociations();
    List<Association> getIncomingAssociations();

    void addAssignment(String child, String parent);
    void removeAssignment(String child, String parent);
    void addAssociation(String ua, String target, AccessRightSet accessRightSet);
    void removeAssociation(String ua, String target);

}
