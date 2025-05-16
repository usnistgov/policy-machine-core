package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.PolicyQuery;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

public class PolicyQueryAdjudicator implements PolicyQuery {

    private final AccessQueryAdjudicator access;
    private final GraphQueryAdjudicator graph;
    private final ProhibitionsQueryAdjudicator prohibitions;
    private final ObligationsQueryAdjudicator obligations;
    private final OperationsQueryAdjudicator operations;
    private final RoutinesQueryAdjudicator routines;
    private final SelfAccessQueryAdjudicator discovery;

    public PolicyQueryAdjudicator(PAP pap, UserContext userCtx) {
        this.access = new AccessQueryAdjudicator(pap, userCtx);
        this.graph = new GraphQueryAdjudicator(pap, userCtx);
        this.prohibitions = new ProhibitionsQueryAdjudicator(pap, userCtx);
        this.obligations = new ObligationsQueryAdjudicator(pap, userCtx);
        this.operations = new OperationsQueryAdjudicator(pap, userCtx);
        this.routines = new RoutinesQueryAdjudicator(pap, userCtx);
        this.discovery = new SelfAccessQueryAdjudicator(pap, userCtx);
    }

    public PolicyQueryAdjudicator(AccessQueryAdjudicator access,
                                  GraphQueryAdjudicator graph,
                                  ProhibitionsQueryAdjudicator prohibitions,
                                  ObligationsQueryAdjudicator obligations,
                                  OperationsQueryAdjudicator operations,
                                  RoutinesQueryAdjudicator routines,
                                  SelfAccessQueryAdjudicator discovery) {
        this.access = access;
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
        this.operations = operations;
        this.routines = routines;
        this.discovery = discovery;
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

    public SelfAccessQueryAdjudicator discovery() {
        return discovery;
    }

}
