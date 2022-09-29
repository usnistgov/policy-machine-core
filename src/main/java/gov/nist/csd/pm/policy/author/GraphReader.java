package gov.nist.csd.pm.policy.author;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.List;
import java.util.Map;

public interface GraphReader {


    AccessRightSet getResourceAccessRights() throws PMException;
    boolean nodeExists(String name) throws PMException;
    Node getNode(String name) throws PMException;
    List<String> search(NodeType type, Map<String, String> properties) throws PMException;
    List<String> getPolicyClasses() throws PMException;
    List<String> getChildren(String node) throws PMException;
    List<String> getParents(String node) throws PMException;
    List<Association> getAssociationsWithSource(String ua) throws PMException;
    List<Association> getAssociationsWithTarget(String target) throws PMException;
}
