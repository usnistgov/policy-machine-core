package gov.nist.csd.pm.pap.op;

import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pdp.exception.UnauthorizedException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
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

    public void check(UserContext userCtx, String target, Collection<String> toCheck) throws PMException {
        TargetContext targetContext = new TargetContext(target);

        AccessRightSet computed = pap.query().access().computePrivileges(userCtx, targetContext);
        if (!computed.containsAll(toCheck)) {
            if (explain) {
                throw new UnauthorizedException(pap.query().access().explain(userCtx, targetContext), userCtx, target, toCheck);
            } else {
                throw new UnauthorizedException(null, userCtx, target, toCheck);
            }
        }
    }

    public void check(UserContext userCtx, String target, String... toCheck) throws PMException {
        check(userCtx, target, Arrays.asList(toCheck));
    }

    public void check(UserContext userCtx, List<String> targets, String... toCheck) throws PMException {
        for (String target : targets) {
            check(userCtx, target, Arrays.asList(toCheck));
        }
    }

    public void check(UserContext userCtx, Collection<String> targets, String... toCheck) throws PMException {
        for (String target : targets) {
            check(userCtx, target, toCheck);
        }
    }

    public void checkPattern(UserContext userCtx, Pattern pattern, String toCheck) throws PMException {
        ReferencedNodes referencedNodes = pattern.getReferencedNodes();
        if (referencedNodes.isAny()) {
            check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(), toCheck);

            return;
        }

        for (String entity : referencedNodes.nodes()) {
            check(userCtx, entity, toCheck);
        }

    }
}
