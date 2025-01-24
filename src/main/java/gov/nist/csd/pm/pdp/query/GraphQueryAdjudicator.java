package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.GraphQuery;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.query.model.subgraph.AscendantSubgraph;
import gov.nist.csd.pm.pap.query.model.subgraph.DescendantSubgraph;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;
import gov.nist.csd.pm.pdp.UnauthorizedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.REVIEW_POLICY;

public class GraphQueryAdjudicator extends Adjudicator implements GraphQuery {

    private final UserContext userCtx;
    private final PAP pap;
    private final PrivilegeChecker privilegeChecker;

    public GraphQueryAdjudicator(UserContext userCtx, PAP pap, PrivilegeChecker privilegeChecker) {
        super(privilegeChecker);
        this.userCtx = userCtx;
        this.pap = pap;
        this.privilegeChecker = privilegeChecker;
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        boolean exists = pap.query().graph().nodeExists(name);

        // check user has permissions on the node
        privilegeChecker.check(userCtx, name);

        return exists;
    }

    @Override
    public Node getNodeByName(String name) throws PMException {
        // get node
        Node node = pap.query().graph().getNodeByName(name);

        // check user has permissions on the node
        privilegeChecker.check(userCtx, name);

        return node;
    }

    @Override
    public Collection<Node> search(NodeType type, Map<String, String> properties) throws PMException {
        Collection<String> search = pap.query().graph().search(type, properties);
        search.removeIf(node -> {
            try {
                privilegeChecker.check(userCtx, node);
                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return search;
    }

    @Override
    public Collection<String> getPolicyClasses() throws PMException {
        List<String> policyClasses = new ArrayList<>();
        for (String pc : pap.query().graph().getPolicyClasses()) {
            try {
                privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeName());
            } catch (UnauthorizedException e) {
                continue;
            }

            policyClasses.add(pc);
        }

        return policyClasses;
    }

    @Override
    public Collection<String> getAdjacentDescendants(long nodeId) throws PMException {
        privilegeChecker.check(userCtx, nodeId, REVIEW_POLICY);

        return pap.query().graph().getAdjacentDescendants(nodeId);
    }

    @Override
    public Collection<String> getAdjacentAscendants(long nodeId) throws PMException {
        privilegeChecker.check(userCtx, nodeId, REVIEW_POLICY);

        return pap.query().graph().getAdjacentAscendants(nodeId);
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
    public AscendantSubgraph getAscendantSubgraph(long nodeId) throws PMException {
        privilegeChecker.check(userCtx, nodeId, REVIEW_POLICY);

        return pap.query().graph().getAscendantSubgraph(nodeId);
    }

    @Override
    public DescendantSubgraph getDescendantSubgraph(long nodeId) throws PMException {
        privilegeChecker.check(userCtx, nodeId, REVIEW_POLICY);

        return pap.query().graph().getDescendantSubgraph(nodeId);
    }

    @Override
    public long[] getAttributeDescendants(long nodeId) throws PMException {
        privilegeChecker.check(userCtx, nodeId, REVIEW_POLICY);

        return pap.query().graph().getAttributeDescendants(nodeId);
    }

    @Override
    public long[] getPolicyClassDescendants(long nodeId) throws PMException {
        privilegeChecker.check(userCtx, nodeId, REVIEW_POLICY);

        return pap.query().graph().getPolicyClassDescendants(nodeId);
    }

    @Override
    public boolean isAscendant(long ascendantId, long descendantId) throws PMException {
        privilegeChecker.check(userCtx, ascendantId, REVIEW_POLICY);
        privilegeChecker.check(userCtx, descendantId, REVIEW_POLICY);

        return pap.query().graph().isAscendant(ascendantId, descendantId);
    }

    @Override
    public boolean isDescendant(long ascendantId, long descendantId) throws PMException {
        privilegeChecker.check(userCtx, ascendantId, REVIEW_POLICY);

        return pap.query().graph().isDescendant(ascendantId, descendantId);
    }

    private List<Association> getAssociations(Collection<Association> associations) {
        List<Association> ret = new ArrayList<>();
        for (Association association : associations) {
            try {
                privilegeChecker.check(userCtx, association.getSource(), REVIEW_POLICY);
                privilegeChecker.check(userCtx, association.getTarget(), REVIEW_POLICY);
            } catch (PMException e) {
                continue;
            }

            ret.add(association);
        }

        return ret;
    }
}
