/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.store;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.common.tx.Transactional;
import gov.nist.ngac.pm.core.pap.graph.Association;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.NodeLookup;
import gov.nist.ngac.pm.core.pap.query.model.subgraph.Subgraph;
import java.util.Collection;
import java.util.Map;

/**
 * The persistence layer for the policy graph.
 */
public interface GraphStore extends Transactional, NodeLookup {

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
