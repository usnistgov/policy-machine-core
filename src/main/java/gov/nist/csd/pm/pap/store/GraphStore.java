package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.node.Node;
import gov.nist.csd.pm.pap.graph.node.NodeType;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.graph.relationship.Association;
import gov.nist.csd.pm.pap.tx.Transactional;

import java.util.Collection;
import java.util.Map;

public interface GraphStore extends Transactional {

    void createNode(String name, NodeType type) throws PMException;
    void deleteNode(String name) throws PMException;
    void setNodeProperties(String name, Map<String, String> properties) throws PMException;
    void createAssignment(String start, String end) throws PMException;
    void deleteAssignment(String start, String end) throws PMException;
    void createAssociation(String ua, String target, AccessRightSet arset) throws PMException;
    void deleteAssociation(String ua, String target) throws PMException;

    Node getNode(String name) throws PMException;
    boolean nodeExists(String name) throws PMException;
    Collection<String> search(NodeType type, Map<String, String> properties) throws PMException;
    Collection<String> getPolicyClasses() throws PMException;
    Collection<String> getAdjacentDescendants(String name) throws PMException;
    Collection<String> getAdjacentAscendants(String name) throws PMException;
    Collection<Association> getAssociationsWithSource(String ua) throws PMException;
    Collection<Association> getAssociationsWithTarget(String target) throws PMException;
    Collection<String> getAscendants(String node) throws PMException;
    Collection<String> getPolicyClassDescendants(String node) throws PMException;
    Collection<String> getAttributeDescendants(String node) throws PMException;
    Collection<String> getDescendants(String node) throws PMException;
    boolean isAscendant(String asc, String dsc) throws PMException;
    boolean isDescendant(String asc, String dsc) throws PMException;
}
