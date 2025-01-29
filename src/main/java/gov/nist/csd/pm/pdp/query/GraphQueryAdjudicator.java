package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.GraphQuery;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.query.model.subgraph.Subgraph;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;
import gov.nist.csd.pm.pdp.UnauthorizedException;
import it.unimi.dsi.fastutil.longs.LongArrayList;

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
    public boolean nodeExists(long id) throws PMException {
        boolean exists = pap.query().graph().nodeExists(id);
        if (!exists) {
            return false;
        }

        // check user has permissions on the node
        privilegeChecker.check(userCtx, id);

        return exists;
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        try {
            Node node = pap.query().graph().getNodeByName(name);

            // check user has permissions on the node
            privilegeChecker.check(userCtx, node.getId());
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
        privilegeChecker.check(userCtx, node.getId());

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
        privilegeChecker.check(userCtx, id);

        return node;
    }

    @Override
    public Collection<Node> search(NodeType type, Map<String, String> properties) throws PMException {
        Collection<Node> search = pap.query().graph().search(type, properties);
        search.removeIf(node -> {
            try {
                privilegeChecker.check(userCtx, node.getId());
                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return search;
    }

    @Override
    public long[] getPolicyClasses() throws PMException {
        LongArrayList policyClasses = new LongArrayList();
        for (long pc : pap.query().graph().getPolicyClasses()) {
            try {
                privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId());
            } catch (UnauthorizedException e) {
                continue;
            }

            policyClasses.add(pc);
        }

        return policyClasses.toLongArray();
    }

    @Override
    public long[] getAdjacentDescendants(long nodeId) throws PMException {
        privilegeChecker.check(userCtx, nodeId, REVIEW_POLICY);

        return pap.query().graph().getAdjacentDescendants(nodeId);
    }

    @Override
    public long[] getAdjacentAscendants(long nodeId) throws PMException {
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
    public Subgraph getAscendantSubgraph(long nodeId) throws PMException {
        privilegeChecker.check(userCtx, nodeId, REVIEW_POLICY);

        return pap.query().graph().getAscendantSubgraph(nodeId);
    }

    @Override
    public Subgraph getDescendantSubgraph(long nodeId) throws PMException {
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
