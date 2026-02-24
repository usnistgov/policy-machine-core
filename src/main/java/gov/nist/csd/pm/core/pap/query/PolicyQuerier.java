package gov.nist.csd.pm.core.pap.query;

public class PolicyQuerier implements PolicyQuery {

    private final GraphQuerier graphQuerier;
    private final ProhibitionsQuerier prohibitionsQuerier;
    private final ObligationsQuerier obligationsQuerier;
    private final OperationsQuerier operationsQuerier;
    private final AccessQuerier accessQuerier;
    private final SelfAccessQuerier selfAccessQuerier;

    public PolicyQuerier(GraphQuerier graphQuerier,
                         ProhibitionsQuerier prohibitionsQuerier,
                         ObligationsQuerier obligationsQuerier,
                         OperationsQuerier operationsQuerier,
                         AccessQuerier accessQuerier,
                         SelfAccessQuerier selfAccessQuerier) {
        this.graphQuerier = graphQuerier;
        this.prohibitionsQuerier = prohibitionsQuerier;
        this.obligationsQuerier = obligationsQuerier;
        this.operationsQuerier = operationsQuerier;
        this.accessQuerier = accessQuerier;
        this.selfAccessQuerier = selfAccessQuerier;
    }

    @Override
    public AccessQuerier access() {
        return accessQuerier;
    }

    @Override
    public SelfAccessQuery selfAccess() {
        return selfAccessQuerier;
    }

    @Override
    public GraphQuerier graph() {
        return graphQuerier;
    }

    @Override
    public ProhibitionsQuerier prohibitions() {
        return prohibitionsQuerier;
    }

    @Override
    public ObligationsQuerier obligations() {
        return obligationsQuerier;
    }

    @Override
    public OperationsQuerier operations() {
        return operationsQuerier;
    }

}