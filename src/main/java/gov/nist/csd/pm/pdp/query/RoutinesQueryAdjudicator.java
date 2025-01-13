package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.AdminAccessRights;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.RoutinesQuery;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.common.routine.Routine;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

import java.util.Collection;

public class RoutinesQueryAdjudicator extends Adjudicator implements RoutinesQuery {

    private PAP pap;
    private UserContext userCtx;
    private final PrivilegeChecker privilegeChecker;

    public RoutinesQueryAdjudicator(UserContext userCtx, PAP pap, PrivilegeChecker privilegeChecker) {
        super(privilegeChecker);
        this.userCtx = userCtx;
        this.pap = pap;
        this.privilegeChecker = privilegeChecker;
    }

    @Override
    public Collection<String> getAdminRoutineNames() throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().routines().getAdminRoutineNames();
    }

    @Override
    public Routine<?> getAdminRoutine(String routineName) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().routines().getAdminRoutine(routineName);
    }
}
