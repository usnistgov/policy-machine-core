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

/**
 * Adjudicate queries on routines. Because routines are not Policy Elements, there are no access checks on
 * querying them. The access checks are enforced during execution.
 */
public class  RoutinesQueryAdjudicator extends Adjudicator implements RoutinesQuery {

    public RoutinesQueryAdjudicator(PAP pap, UserContext userCtx) {
        super(pap, userCtx);
    }

    @Override
    public Collection<String> getAdminRoutineNames() throws PMException {
        return pap.query().routines().getAdminRoutineNames();
    }

    @Override
    public Routine<?> getAdminRoutine(String routineName) throws PMException {
        return pap.query().routines().getAdminRoutine(routineName);
    }
}
