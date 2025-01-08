package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.*;

public class PolicyQueryAdjudicator implements PolicyQuery {

    private final AccessQueryAdjudicator access;
    private final GraphQueryAdjudicator graph;
    private final ProhibitionsQueryAdjudicator prohibitions;
    private final ObligationsQueryAdjudicator obligations;
    private final OperationsQueryAdjudicator operations;
    private final RoutinesQueryAdjudicator routines;

    public PolicyQueryAdjudicator(UserContext userCtx, PAP pap, PrivilegeChecker privilegeChecker) {
        this.access = new AccessQueryAdjudicator(userCtx, pap, privilegeChecker);
        this.graph = new GraphQueryAdjudicator(userCtx, pap, privilegeChecker);
        this.prohibitions = new ProhibitionsQueryAdjudicator(userCtx, pap, privilegeChecker);
        this.obligations = new ObligationsQueryAdjudicator(userCtx, pap, privilegeChecker);
        this.operations = new OperationsQueryAdjudicator(userCtx, pap, privilegeChecker);
        this.routines = new RoutinesQueryAdjudicator(userCtx, pap, privilegeChecker);
    }

    @Override
    public AccessQueryAdjudicator access() {
        return access;
    }

    @Override
    public GraphQueryAdjudicator graph() {
        return graph;
    }

    @Override
    public ProhibitionsQueryAdjudicator prohibitions() {
        return prohibitions;
    }

    @Override
    public ObligationsQueryAdjudicator obligations() {
        return obligations;
    }

    @Override
    public OperationsQueryAdjudicator operations() {
        return operations;
    }

    @Override
    public RoutinesQueryAdjudicator routines() {
        return routines;
    }

}
