package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.pap.admin.AdminAccessRights;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.RoutinesQuery;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

import java.util.Collection;

public class  RoutinesQueryAdjudicator extends Adjudicator implements RoutinesQuery {

    public RoutinesQueryAdjudicator(PAP pap, UserContext userCtx) {
        super(pap, userCtx);
    }

    @Override
    public Collection<String> getAdminRoutineNames() throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().routines().getAdminRoutineNames();
    }

    @Override
    public Routine<?, ?> getAdminRoutine(String routineName) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), AdminAccessRights.REVIEW_POLICY);

        return pap.query().routines().getAdminRoutine(routineName);
    }
}
