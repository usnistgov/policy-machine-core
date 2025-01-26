package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pdp.UnauthorizedException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PrivilegeChecker {

    private PAP pap;
    private boolean explain;

    public PrivilegeChecker(PAP pap) {
        this.pap = pap;
        this.explain = false;
    }

    public boolean isExplain() {
        return explain;
    }

    public void setExplain(boolean explain) {
        this.explain = explain;
    }

    public NodeType getNodeType(long id) throws PMException {
        return pap.query().graph().getNodeById(id).getType();
    }

    public void check(UserContext userCtx, long target, Collection<String> toCheck) throws PMException {
        TargetContext targetContext = new TargetContext(target);

        AccessRightSet computed = pap.query().access().computePrivileges(userCtx, targetContext);

        checkOrThrow(userCtx, targetContext, computed, toCheck);
    }

    public void check(UserContext userCtx, UserContext target, Collection<String> toCheck) throws PMException {
        TargetContext targetContext = new TargetContext(target);

        AccessRightSet computed = pap.query().access().computePrivileges(userCtx, targetContext);

        checkOrThrow(userCtx, targetContext, computed, toCheck);
    }

    public void check(UserContext userCtx, TargetContext targetContext, Collection<String> toCheck) throws PMException {
        AccessRightSet computed = pap.query().access().computePrivileges(userCtx, targetContext);

        checkOrThrow(userCtx, targetContext, computed, toCheck);
    }

    public void check(UserContext userCtx, long target, String... toCheck) throws PMException {
        check(userCtx, target, Arrays.asList(toCheck));
    }

    public void check(UserContext userCtx, List<Long> targets, String... toCheck) throws PMException {
        for (long target : targets) {
            check(userCtx, target, Arrays.asList(toCheck));
        }
    }

    public void checkPattern(UserContext userCtx, Pattern pattern, String toCheck) throws PMException {
        ReferencedNodes referencedNodes = pattern.getReferencedNodes();
        if (referencedNodes.isAny()) {
            check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), toCheck);

            return;
        }

        for (long entity : referencedNodes.nodes()) {
            check(userCtx, entity, toCheck);
        }
    }

    private void checkOrThrow(UserContext userContext, TargetContext targetContext, AccessRightSet computed, Collection<String> toCheck) throws PMException {
        if (!computed.containsAll(toCheck) || (toCheck.isEmpty() && computed.isEmpty())) {
            if (explain) {
                throw new UnauthorizedException(
                        pap.query().access().explain(userContext, targetContext),
                        userContext,
                        targetContext,
                        toCheck
                );
            } else {
                throw new UnauthorizedException(null, userContext, targetContext, toCheck);
            }
        }
    }

    private void checkOrThrow(UserContext userCtx, List<TargetContext> targetContexts, List<AccessRightSet> privileges, Collection<String> toCheck) throws PMException {
        for (int i = 0; i < targetContexts.size(); i++) {
            TargetContext targetContext = targetContexts.get(i);
            AccessRightSet privs = privileges.get(i);

            checkOrThrow(userCtx, targetContext, privs, toCheck);
        }
    }
}
