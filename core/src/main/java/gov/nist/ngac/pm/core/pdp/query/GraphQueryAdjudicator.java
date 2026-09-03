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

package gov.nist.ngac.pm.core.pdp.query;

import gov.nist.ngac.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.ngac.pm.core.pap.graph.Association;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.query.GraphQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.query.model.subgraph.Subgraph;
import gov.nist.ngac.pm.core.pdp.UnauthorizedException;
import gov.nist.ngac.pm.core.pdp.adjudication.Adjudicator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A {@link GraphQuery} that checks the acting user's admin privileges before delegating to the PAP.
 */
public class GraphQueryAdjudicator extends Adjudicator implements GraphQuery {

    public GraphQueryAdjudicator(PAP pap, UserContext userCtx) {
        super(pap, userCtx);
    }

    @Override
    public boolean nodeExists(long id) throws PMException {
        boolean exists = pap.query().graph().nodeExists(id);
        if (!exists) {
            return false;
        }

        // check user has permissions on the node
        check(userCtx, NodeTargetContext.of(id));

        return true;
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        try {
            Node node = pap.query().graph().getNodeByName(name);

            // check user has permissions on the node
            check(userCtx, NodeTargetContext.of(node.getId()));
        } catch (NodeDoesNotExistException e) {
            return false;
        }

        return true;
    }

    @Override
    public Node getNodeByName(String name) throws PMException {
        // get node
        Node node = pap.query().graph().getNodeByName(name);

        // check user has permissions on the node
        check(userCtx, NodeTargetContext.of(node.getId()));

        return node;
    }

    @Override
    public long getNodeId(String name) throws PMException {
        return getNodeByName(name).getId();
    }

    @Override
    public Node getNodeById(long id) throws PMException {
        // get node
        Node node = pap.query().graph().getNodeById(id);

        // check user has permissions on the node
        check(userCtx, NodeTargetContext.of(id));

        return node;
    }

    @Override
    public Collection<Node> search(NodeType type, Map<String, String> properties) throws PMException {
        Collection<Node> search = pap.query().graph().search(type, properties);
        search.removeIf(node -> {
            try {
                check(userCtx, NodeTargetContext.of(node.getId()));
                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return search;
    }

    @Override
    public Collection<Long> getPolicyClasses() throws PMException {
        check(userCtx, NodeTargetContext.of(AdminPolicyNode.PM_ADMIN_POLICY_CLASSES.nodeId()),
            AdminAccessRight.ADMIN_GRAPH_POLICY_CLASS_LIST);

        return pap.query().graph().getPolicyClasses();
    }

    @Override
    public Collection<Long> getAdjacentDescendants(long nodeId) throws PMException {
        check(userCtx, NodeTargetContext.of(nodeId), AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_LIST);

        return filterNodes(pap.query().graph().getAdjacentDescendants(nodeId), AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_LIST);
    }

    @Override
    public Collection<Long> getAdjacentAscendants(long nodeId) throws PMException {
        check(userCtx, NodeTargetContext.of(nodeId), AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_LIST);

        return filterNodes(pap.query().graph().getAdjacentAscendants(nodeId), AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_LIST);
    }

    @Override
    public Collection<Association> getAssociationsWithSource(long uaId) throws PMException {
        return getAssociations(pap.query().graph().getAssociationsWithSource(uaId));
    }

    @Override
    public Collection<Association> getAssociationsWithTarget(long targetId) throws PMException {
        return getAssociations(pap.query().graph().getAssociationsWithTarget(targetId));
    }

    @Override
    public Subgraph getAscendantSubgraph(long nodeId) throws PMException {
        check(userCtx, NodeTargetContext.of(nodeId), AdminAccessRight.ADMIN_GRAPH_SUBGRAPH_LIST);

        return pap.query().graph().getAscendantSubgraph(nodeId);
    }

    @Override
    public Subgraph getDescendantSubgraph(long nodeId) throws PMException {
        check(userCtx, NodeTargetContext.of(nodeId), AdminAccessRight.ADMIN_GRAPH_SUBGRAPH_LIST);

        return pap.query().graph().getDescendantSubgraph(nodeId);
    }

    @Override
    public Collection<Long> getAttributeDescendants(long nodeId) throws PMException {
        check(userCtx, NodeTargetContext.of(nodeId), AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_LIST);

        Collection<Long> attributeDescendants = pap.query().graph().getAttributeDescendants(nodeId);
        return filterNodes(attributeDescendants, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_LIST);
    }

    @Override
    public Collection<Long> getPolicyClassDescendants(long nodeId) throws PMException {
        check(userCtx, NodeTargetContext.of(nodeId), AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_LIST);

        Collection<Long> policyClassDescendants = pap.query().graph().getPolicyClassDescendants(nodeId);
        return filterNodes(policyClassDescendants, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_LIST);
    }

    @Override
    public boolean isAscendant(long ascendantId, long descendantId) throws PMException {
        check(userCtx, NodeTargetContext.of(ascendantId), AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_LIST);
        check(userCtx, NodeTargetContext.of(descendantId), AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_LIST);

        return pap.query().graph().isAscendant(ascendantId, descendantId);
    }

    @Override
    public boolean isDescendant(long ascendantId, long descendantId) throws PMException {
        check(userCtx, NodeTargetContext.of(ascendantId), AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_LIST);
        check(userCtx, NodeTargetContext.of(descendantId), AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_LIST);

        return pap.query().graph().isDescendant(ascendantId, descendantId);
    }

    private List<Association> getAssociations(Collection<Association> associations) throws PMException {
        List<Association> ret = new ArrayList<>();
        for (Association association : associations) {
            try {
                check(userCtx, NodeTargetContext.of(association.source()), AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_LIST);
                check(userCtx, NodeTargetContext.of(association.target()), AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_LIST);
            } catch (UnauthorizedException e) {
                continue;
            } catch (PMException e) {
                throw e;
            }

            ret.add(association);
        }

        return ret;
    }

    Collection<Long> filterNodes(Collection<Long> nodes, AdminAccessRight ar) throws PMException {
        filterUnauthorized(nodes, id -> check(userCtx, NodeTargetContext.of(id), ar));

        return nodes;
    }
}
