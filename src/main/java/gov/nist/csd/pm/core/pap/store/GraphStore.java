package gov.nist.csd.pm.core.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.common.tx.Transactional;
import gov.nist.csd.pm.core.pap.query.model.subgraph.Subgraph;
import java.util.Collection;
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
    Collection<Long> search(NodeType type, Map<String, String> properties) throws PMException;
    Collection<Long> getPolicyClasses() throws PMException;
    Collection<Long> getAdjacentDescendants(long id) throws PMException;
    Collection<Long> getAdjacentAscendants(long id) throws PMException;
    Collection<Association> getAssociationsWithSource(long uaId) throws PMException;
    Collection<Association> getAssociationsWithTarget(long targetId) throws PMException;
    Collection<Long> getPolicyClassDescendants(long id) throws PMException;
    Collection<Long> getAttributeDescendants(long id) throws PMException;
    Subgraph getDescendantSubgraph(long id) throws PMException;
    Subgraph getAscendantSubgraph(long id) throws PMException;
    boolean isAscendant(long asc, long dsc) throws PMException;
    boolean isDescendant(long asc, long dsc) throws PMException;
}
