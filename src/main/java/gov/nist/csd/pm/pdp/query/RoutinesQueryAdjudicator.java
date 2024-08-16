package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.op.AdminAccessRights;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.RoutinesQuerier;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.routine.Routine;

import java.util.Collection;

public class RoutinesQueryAdjudicator extends RoutinesQuerier {

    private PAP pap;
    private UserContext userCtx;

    public RoutinesQueryAdjudicator(UserContext userCtx, PAP pap) {
        super(pap.query());

        this.pap = pap;
        this.userCtx = userCtx;
    }

    @Override
    public Collection<String> getAdminRoutineNames() throws PMException {
        PrivilegeChecker.check(pap, userCtx, AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().routines().getAdminRoutineNames();
    }

    @Override
    public Routine<?> getAdminRoutine(String routineName) throws PMException {
        PrivilegeChecker.check(pap, userCtx, AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().routines().getAdminRoutine(routineName);
    }
}
