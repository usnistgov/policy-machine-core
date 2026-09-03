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

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.query.AccessQuery;
import gov.nist.ngac.pm.core.pap.query.SelfAccessQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.AnonymousTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.AnonymousUserContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.query.model.explain.Explain;
import gov.nist.ngac.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.ngac.pm.core.pdp.adjudication.Adjudicator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A {@link AccessQuery} that checks the acting user's admin privileges before delegating to the PAP.
 */
public class AccessQueryAdjudicator extends Adjudicator implements AccessQuery {

    public AccessQueryAdjudicator(PAP pap, UserContext userCtx) {
        super(pap, userCtx);
    }

    @Override
    public SelfAccessQuery self(UserContext userCtx) throws PMException {
        return new SelfAccessQueryAdjudicator(pap, userCtx);
    }

    @Override
    public AccessRightSet computePrivileges(UserContext userCtx, TargetContext targetCtx) throws PMException {
        checkOnUserCtx(userCtx);
        check(this.userCtx, targetCtx, AdminAccessRight.ADMIN_ACCESS_QUERY);

        return pap.query().access().computePrivileges(userCtx, targetCtx);
    }

    @Override
    public List<AccessRightSet> computePrivileges(UserContext userCtx, List<TargetContext> targetCtxs) throws PMException {
        checkOnUserCtx(userCtx);

        for (TargetContext targetCtx : targetCtxs) {
            check(this.userCtx, targetCtx, AdminAccessRight.ADMIN_ACCESS_QUERY);
        }

        return pap.query().access().computePrivileges(userCtx, targetCtxs);
    }

    @Override
    public AccessRightSet computeDeniedPrivileges(UserContext userCtx, TargetContext targetCtx) throws PMException {
        checkOnUserCtx(userCtx);
        check(this.userCtx, targetCtx, AdminAccessRight.ADMIN_ACCESS_QUERY);

        return pap.query().access().computeDeniedPrivileges(userCtx, targetCtx);
    }

    @Override
    public Map<Long, AccessRightSet> computeCapabilityList(UserContext userCtx) throws PMException {
        checkOnUserCtx(userCtx);

        return pap.query().access().computeCapabilityList(userCtx);
    }

    @Override
    public Map<Long, AccessRightSet> computeACL(TargetContext targetCtx) throws PMException {
        check(this.userCtx, targetCtx, AdminAccessRight.ADMIN_ACCESS_QUERY);

        return pap.query().access().computeACL(targetCtx);
    }

    @Override
    public Map<Long, AccessRightSet> computeDestinationAttributes(UserContext userCtx) throws PMException {
        checkOnUserCtx(userCtx);

        return pap.query().access().computeDestinationAttributes(userCtx);
    }

    @Override
    public SubgraphPrivileges computeSubgraphPrivileges(UserContext userCtx, long root) throws PMException {
        checkOnUserCtx(userCtx);
        check(this.userCtx, NodeTargetContext.of(root), AdminAccessRight.ADMIN_ACCESS_QUERY);

        return pap.query().access().computeSubgraphPrivileges(userCtx, root);
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(UserContext userCtx, long root) throws PMException {
        checkOnUserCtx(userCtx);
        check(this.userCtx, NodeTargetContext.of(root), AdminAccessRight.ADMIN_ACCESS_QUERY);

        return pap.query().access().computeAdjacentAscendantPrivileges(userCtx, root);
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(UserContext userCtx, long root) throws PMException {
        checkOnUserCtx(userCtx);
        check(this.userCtx, NodeTargetContext.of(root), AdminAccessRight.ADMIN_ACCESS_QUERY);

        return pap.query().access().computeAdjacentDescendantPrivileges(userCtx, root);
    }

    @Override
    public Explain explain(UserContext userCtx, TargetContext targetCtx) throws PMException {
        checkOnUserCtx(userCtx);
        check(this.userCtx, targetCtx, AdminAccessRight.ADMIN_ACCESS_QUERY);

        return pap.query().access().explain(userCtx, targetCtx);
    }

    @Override
    public Map<Node, AccessRightSet> computePersonalObjectSystem(UserContext userCtx) throws PMException {
        checkOnUserCtx(userCtx);

        return pap.query().access().computePersonalObjectSystem(userCtx);
    }

    @Override
    public Map<Long, Set<Long>> computeRequiredAttributeSets(TargetContext targetCtx, AccessRightSet privileges) throws
                                                                                                                 PMException {
        check(this.userCtx, targetCtx, AdminAccessRight.ADMIN_ACCESS_QUERY);
        return pap.query().access().computeRequiredAttributeSets(targetCtx, privileges);
    }

    private void checkOnUserCtx(UserContext userCtx) throws PMException {
        switch (userCtx) {
            case NodeUserContext ctx -> {
                long id = ctx.resolveNodeIds(pap.query().graph()).iterator().next();
                check(this.userCtx, NodeTargetContext.of(id), AdminAccessRight.ADMIN_ACCESS_QUERY);
            }
            case AnonymousUserContext ctx -> {
                Set<Long> ids = new HashSet<>(ctx.resolveNodeIds(pap.query().graph()));
                check(this.userCtx, AnonymousTargetContext.ofIds(ids), AdminAccessRight.ADMIN_ACCESS_QUERY);
            }
            default -> throw new IllegalArgumentException("unsupported user context type: " + userCtx.getClass());
        }
    }
}
