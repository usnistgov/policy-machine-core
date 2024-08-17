package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.*;

public class PolicyQueryAdjudicator extends PolicyQuerier {

    private final AccessQueryAdjudicator access;
    private final GraphQueryAdjudicator graph;
    private final ProhibitionsQueryAdjudicator prohibitions;
    private final ObligationsQueryAdjudicator obligations;
    private final OperationsQueryAdjudicator operations;
    private final RoutinesQueryAdjudicator routines;

    public PolicyQueryAdjudicator(UserContext userCtx, PAP pap) {
        super(pap.query());
        this.access = new AccessQueryAdjudicator(userCtx, pap);
        this.graph = new GraphQueryAdjudicator(userCtx, pap);
        this.prohibitions = new ProhibitionsQueryAdjudicator(userCtx, pap);
        this.obligations = new ObligationsQueryAdjudicator(userCtx, pap);
        this.operations = new OperationsQueryAdjudicator(userCtx, pap);
        this.routines = new RoutinesQueryAdjudicator(userCtx, pap);
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
    public RoutinesQuerier routines() {
        return routines;
    }

}
