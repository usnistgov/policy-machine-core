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

package gov.nist.ngac.pm.core.pap.query;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A {@link SelfAccessQuery} implementation that delegates to an {@link AccessQuerier}, fixed to a
 * single user context.
 */
public class SelfAccessQuerier implements SelfAccessQuery{

    private AccessQuerier accessQuerier;
    private UserContext userCtx;

    public SelfAccessQuerier(AccessQuerier accessQuerier, UserContext userCtx) {
        this.accessQuerier = accessQuerier;
        this.userCtx = userCtx;
    }

    @Override
    public AccessRightSet computePrivileges(TargetContext targetCtx) throws PMException {
        return accessQuerier.computePrivileges(userCtx, targetCtx);
    }

    @Override
    public List<AccessRightSet> computePrivileges(List<TargetContext> targetCtxs) throws PMException {
        return accessQuerier.computePrivileges(userCtx, targetCtxs);
    }

    @Override
    public AccessRightSet computeDeniedPrivileges(TargetContext targetCtx) throws PMException {
        return accessQuerier.computeDeniedPrivileges(userCtx, targetCtx);
    }

    @Override
    public SubgraphPrivileges computeSubgraphPrivileges(long root) throws PMException {
        return accessQuerier.computeSubgraphPrivileges(userCtx, root);
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(long root) throws PMException {
        return accessQuerier.computeAdjacentAscendantPrivileges(userCtx, root);
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(long root) throws PMException {
        return accessQuerier.computeAdjacentDescendantPrivileges(userCtx, root);
    }

    @Override
    public Map<Node, AccessRightSet> computePersonalObjectSystem() throws PMException {
        return accessQuerier.computePersonalObjectSystem(userCtx);
    }

    @Override
    public Map<Long, Set<Long>> computeRequiredAttributeSets(TargetContext targetCtx, AccessRightSet privileges) throws PMException {
        return accessQuerier.computeRequiredAttributeSets(targetCtx, privileges);
    }

    @Override
    public Map<Long, AccessRightSet> computeCapabilityList() throws PMException {
        return accessQuerier.computeCapabilityList(userCtx);
    }
}