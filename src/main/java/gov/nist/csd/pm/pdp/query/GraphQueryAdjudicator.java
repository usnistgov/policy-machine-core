package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.node.Node;
import gov.nist.csd.pm.pap.graph.node.NodeType;
import gov.nist.csd.pm.pap.graph.relationship.Association;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.GraphQuerier;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pdp.exception.UnauthorizedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.REVIEW_POLICY;

public class GraphQueryAdjudicator extends GraphQuerier {

    private final UserContext userCtx;
    private final PAP pap;

    public GraphQueryAdjudicator(UserContext userCtx, PAP pap) {
        super(pap.query());
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        boolean exists = pap.query().graph().nodeExists(name);

        // check user has permissions on the node
        PrivilegeChecker.check(pap, userCtx, name);

        return exists;
    }

    @Override
    public Node getNode(String name) throws PMException {
        // get node
        Node node = pap.query().graph().getNode(name);

        // check user has permissions on the node
        PrivilegeChecker.check(pap, userCtx, name);

        return node;
    }

    @Override
    public Collection<String> search(NodeType type, Map<String, String> properties) throws PMException {
        Collection<String> search = pap.query().graph().search(type, properties);
        search.removeIf(node -> {
            try {
                PrivilegeChecker.check(pap, userCtx, node);
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
                PrivilegeChecker.check(pap, userCtx, AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName());
            } catch (UnauthorizedException e) {
                continue;
            }

            policyClasses.add(pc);
        }

        return policyClasses;
    }

    @Override
    public Collection<String> getAdjacentDescendants(String node) throws PMException {
        List<String> descendants = new ArrayList<>();
        for (String descendant : pap.query().graph().getAdjacentDescendants(node)) {
            try {
                PrivilegeChecker.check(pap, userCtx, descendant);
            } catch (UnauthorizedException e) {
                continue;
            }

            descendants.add(descendant);
        }

        return descendants;
    }

    @Override
    public Collection<String> getAdjacentAscendants(String node) throws PMException {
        List<String> ascendants = new ArrayList<>();
        for (String ascendant : pap.query().graph().getAdjacentAscendants(node)) {
            try {
                PrivilegeChecker.check(pap, userCtx, ascendant);
            } catch (UnauthorizedException e) {
                continue;
            }

            ascendants.add(ascendant);
        }

        return ascendants;
    }

    @Override
    public Collection<Association> getAssociationsWithSource(String ua) throws PMException {
        return getAssociations(pap.query().graph().getAssociationsWithSource(ua));
    }

    @Override
    public Collection<Association> getAssociationsWithTarget(String target) throws PMException {
        return getAssociations(pap.query().graph().getAssociationsWithTarget(target));
    }

    @Override
    public Collection<String> getAttributeDescendants(String node) throws PMException {
        PrivilegeChecker.check(pap, userCtx, node, REVIEW_POLICY);

        return pap.query().graph().getAttributeDescendants(node);
    }

    @Override
    public Collection<String> getPolicyClassDescendants(String node) throws PMException {
        PrivilegeChecker.check(pap, userCtx, node, REVIEW_POLICY);

        return pap.query().graph().getPolicyClassDescendants(node);
    }

    @Override
    public boolean isAscendant(String ascendant, String descendant) throws PMException {
        PrivilegeChecker.check(pap, userCtx, ascendant, REVIEW_POLICY);
        PrivilegeChecker.check(pap, userCtx, descendant, REVIEW_POLICY);

        return pap.query().graph().isAscendant(ascendant, descendant);
    }

    @Override
    public boolean isDescendant(String ascendant, String descendant) throws PMException {
        PrivilegeChecker.check(pap, userCtx, ascendant, REVIEW_POLICY);

        return pap.query().graph().isDescendant(ascendant, descendant);
    }

    @Override
    public Collection<String> getAscendants(String node) throws PMException {
        PrivilegeChecker.check(pap, userCtx, node, REVIEW_POLICY);

        return pap.query().graph().getAscendants(node);
    }

    @Override
    public Collection<String> getDescendants(String node) throws PMException {
        PrivilegeChecker.check(pap, userCtx, node, REVIEW_POLICY);

        return pap.query().graph().getDescendants(node);
    }

    private List<Association> getAssociations(Collection<Association> associations) {
        List<Association> ret = new ArrayList<>();
        for (Association association : associations) {
            try {
                PrivilegeChecker.check(pap, userCtx, association.getSource(), REVIEW_POLICY);
                PrivilegeChecker.check(pap, userCtx, association.getTarget(), REVIEW_POLICY);
            } catch (PMException e) {
                continue;
            }

            ret.add(association);
        }

        return ret;
    }
}
