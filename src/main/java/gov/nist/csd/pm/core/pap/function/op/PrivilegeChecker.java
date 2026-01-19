package gov.nist.csd.pm.core.pap.function.op;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.AccessQuerier;
import gov.nist.csd.pm.core.pap.query.GraphQuerier;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PrivilegeChecker {

    private final AccessQuerier accessQuerier;
    private final GraphQuerier graphQuerier;

    public PrivilegeChecker(AccessQuerier accessQuerier, GraphQuerier graphQuerier) {
        this.accessQuerier = accessQuerier;
        this.graphQuerier = graphQuerier;
    }

    public void check(UserContext userCtx, TargetContext targetCtx, Collection<String> rightsToCheck) throws PMException {
        AccessRightSet computed = accessQuerier.computePrivileges(userCtx, targetCtx);
        checkOrThrow(userCtx, targetCtx, computed, rightsToCheck);
    }

    public void check(UserContext userCtx, TargetContext targetCtx, String ... rightsToCheck) throws PMException {
        AccessRightSet computed = accessQuerier.computePrivileges(userCtx, targetCtx);
        checkOrThrow(userCtx, targetCtx, computed, List.of(rightsToCheck));
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

    public void check(UserContext userCtx, UserContext targetUserCtx, String ... rightsToCheck) throws PMException {
        check(userCtx, new TargetContext(targetUserCtx), List.of(rightsToCheck));
    }

    public void check(UserContext userCtx, List<Long> targetIds, String... rightsToCheck) throws PMException {
        List<String> rightsList = Arrays.asList(rightsToCheck);
        for (long targetId : targetIds) {
            check(userCtx, targetId, rightsList);
        }
    }

    private void checkOrThrow(UserContext userCtx, TargetContext targetCtx, AccessRightSet computed,
                              Collection<String> required) throws PMException {
        if (!computed.containsAll(required) || (required.isEmpty() && computed.isEmpty())) {
            throw UnauthorizedException.of(graphQuerier, userCtx, targetCtx, computed, required);
        }
    }
}

