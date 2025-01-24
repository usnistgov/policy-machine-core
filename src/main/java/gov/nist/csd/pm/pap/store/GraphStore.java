package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.query.model.subgraph.AscendantSubgraph;
import gov.nist.csd.pm.pap.query.model.subgraph.DescendantSubgraph;
import gov.nist.csd.pm.common.tx.Transactional;

import java.util.Map;

public interface GraphStore extends Transactional {

    void createNode(long id, String name, NodeType type) throws PMException;
    void deleteNode(long id) throws PMException;
    void setNodeProperties(long name, Map<String, String> properties) throws PMException;
    void createAssignment(long start, long end) throws PMException;
    void deleteAssignment(long start, long end) throws PMException;
    void createAssociation(long ua, long target, AccessRightSet arset) throws PMException;
    void deleteAssociation(long ua, long target) throws PMException;

    Node getNodeById(long name) throws PMException;
    Node getNodeByName(String name) throws PMException;
    boolean nodeExists(long id) throws PMException;
    long[] search(NodeType type, Map<String, String> properties) throws PMException;
    long[] getPolicyClasses() throws PMException;
    long[] getAdjacentDescendants(String name) throws PMException;
    long[] getAdjacentAscendants(String name) throws PMException;
    Association[] getAssociationsWithSource(String ua) throws PMException;
    Association[] getAssociationsWithTarget(String target) throws PMException;
    long[] getPolicyClassDescendants(String node) throws PMException;
    long[] getAttributeDescendants(String node) throws PMException;
    DescendantSubgraph getDescendantSubgraph(String node) throws PMException;
    AscendantSubgraph getAscendantSubgraph(String node) throws PMException;
    boolean isAscendant(long asc, long dsc) throws PMException;
    boolean isDescendant(long asc, long dsc) throws PMException;
}
