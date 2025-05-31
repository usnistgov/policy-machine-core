package gov.nist.csd.pm.core.pdp.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.admin.AdminAccessRights;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.query.RoutinesQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;

import java.util.Collection;

public class  RoutinesQueryAdjudicator extends Adjudicator implements RoutinesQuery {

    public RoutinesQueryAdjudicator(PAP pap, UserContext userCtx) {
        super(pap, userCtx);
    }

    @Override
    public Collection<String> getAdminRoutineNames() throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_ROUTINES.nodeId(), AdminAccessRights.QUERY_ADMIN_ROUTINES);

        return pap.query().routines().getAdminRoutineNames();
    }

    @Override
    public Routine<?, ?> getAdminRoutine(String routineName) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_ROUTINES.nodeId(), AdminAccessRights.QUERY_ADMIN_ROUTINES);

        return pap.query().routines().getAdminRoutine(routineName);
    }
}
