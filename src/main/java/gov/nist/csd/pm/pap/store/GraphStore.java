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
    void setNodeProperties(long id, Map<String, String> properties) throws PMException;
    void createAssignment(long start, long end) throws PMException;
    void deleteAssignment(long start, long end) throws PMException;
    void createAssociation(long ua, long target, AccessRightSet arset) throws PMException;
    void deleteAssociation(long ua, long target) throws PMException;

    Node getNodeById(long id) throws PMException;
    Node getNodeByName(String name) throws PMException;
    boolean nodeExists(long id) throws PMException;
    boolean nodeExists(String name) throws PMException;
    long[] search(NodeType type, Map<String, String> properties) throws PMException;
    long[] getPolicyClasses() throws PMException;
    long[] getAdjacentDescendants(long id) throws PMException;
    long[] getAdjacentAscendants(long id) throws PMException;
    Association[] getAssociationsWithSource(long uaId) throws PMException;
    Association[] getAssociationsWithTarget(long targetId) throws PMException;
    long[] getPolicyClassDescendants(long id) throws PMException;
    long[] getAttributeDescendants(long id) throws PMException;
    DescendantSubgraph getDescendantSubgraph(long id) throws PMException;
    AscendantSubgraph getAscendantSubgraph(long id) throws PMException;
    boolean isAscendant(long asc, long dsc) throws PMException;
    boolean isDescendant(long asc, long dsc) throws PMException;
}
