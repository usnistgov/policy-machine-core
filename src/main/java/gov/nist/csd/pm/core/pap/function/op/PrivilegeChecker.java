package gov.nist.csd.pm.core.pap.function.op;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.core.pap.query.AccessQuerier;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PrivilegeChecker {

    private final AccessQuerier accessQuerier;
    private boolean explain;

    public PrivilegeChecker(AccessQuerier accessQuerier) {
        this(accessQuerier, false);
    }
    
    public PrivilegeChecker(AccessQuerier accessQuerier, boolean explain) {
        this.accessQuerier = accessQuerier;
        this.explain = explain;
    }

    public void setExplain(boolean explain) {
        this.explain = explain;
    }

    public boolean isExplain() {
        return explain;
    }

    public void check(UserContext userCtx, TargetContext targetCtx, Collection<String> rightsToCheck) throws PMException {
        AccessRightSet computed = accessQuerier.computePrivileges(userCtx, targetCtx);
        checkOrThrow(userCtx, targetCtx, computed, rightsToCheck);
    }

    public void check(UserContext userCtx, long targetId, Collection<String> rightsToCheck) throws PMException {
        check(userCtx, new TargetContext(targetId), rightsToCheck);
    }

    public void check(UserContext userCtx, long targetId, String... rightsToCheck) throws PMException {
        check(userCtx, targetId, Arrays.asList(rightsToCheck));
    }

    public void check(UserContext userCtx, UserContext targetUserCtx, Collection<String> rightsToCheck) throws PMException {
        check(userCtx, new TargetContext(targetUserCtx), rightsToCheck);
    }

    public void check(UserContext userCtx, List<Long> targetIds, String... rightsToCheck) throws PMException {
        List<String> rightsList = Arrays.asList(rightsToCheck);
        for (long targetId : targetIds) {
            check(userCtx, targetId, rightsList);
        }
    }

    private void checkOrThrow(UserContext userCtx, TargetContext targetCtx, AccessRightSet computed,
                              Collection<String> rightsToCheck) throws PMException {
        if (!computed.containsAll(rightsToCheck) || (rightsToCheck.isEmpty() && computed.isEmpty())) {
            if (explain) {
                throw new UnauthorizedException(
                        accessQuerier.explain(userCtx, targetCtx),
                        userCtx,
                        targetCtx,
                        rightsToCheck
                );
            } else {
                throw new UnauthorizedException(null, userCtx, targetCtx, rightsToCheck);
            }
        }
    }
}

