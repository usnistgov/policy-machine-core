package gov.nist.csd.pm.core.pdp.query;

import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

public class PolicyQueryAdjudicator implements PolicyQuery {

    private final AccessQueryAdjudicator access;
    private final GraphQueryAdjudicator graph;
    private final ProhibitionsQueryAdjudicator prohibitions;
    private final ObligationsQueryAdjudicator obligations;
    private final OperationsQueryAdjudicator operations;
    private final SelfAccessQueryAdjudicator selfAccess;

    public PolicyQueryAdjudicator(PAP pap, UserContext userCtx) {
        this.access = new AccessQueryAdjudicator(pap, userCtx);
        this.graph = new GraphQueryAdjudicator(pap, userCtx);
        this.prohibitions = new ProhibitionsQueryAdjudicator(pap, userCtx);
        this.obligations = new ObligationsQueryAdjudicator(pap, userCtx);
        this.operations = new OperationsQueryAdjudicator(pap, userCtx);
        this.selfAccess = new SelfAccessQueryAdjudicator(pap, userCtx);
    }

    public PolicyQueryAdjudicator(AccessQueryAdjudicator access,
                                  GraphQueryAdjudicator graph,
                                  ProhibitionsQueryAdjudicator prohibitions,
                                  ObligationsQueryAdjudicator obligations,
                                  OperationsQueryAdjudicator operations,
                                  SelfAccessQueryAdjudicator selfAccess) {
        this.access = access;
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
        this.operations = operations;
        this.selfAccess = selfAccess;
    }

    @Override
    public AccessQueryAdjudicator access() {
        return access;
    }

    public SelfAccessQueryAdjudicator selfAccess() {
        return selfAccess;
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

}
