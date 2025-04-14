package gov.nist.csd.pm.pap.query;

public class PolicyQuerier implements PolicyQuery {

    private final GraphQuerier graphQuerier;
    private final ProhibitionsQuerier prohibitionsQuerier;
    private final ObligationsQuerier obligationsQuerier;
    private final OperationsQuerier operationsQuerier;
    private final RoutinesQuerier routinesQuerier;
    private final AccessQuerier accessQuerier;

    public PolicyQuerier(GraphQuerier graphQuerier,
                         ProhibitionsQuerier prohibitionsQuerier,
                         ObligationsQuerier obligationsQuerier,
                         OperationsQuerier operationsQuerier,
                         RoutinesQuerier routinesQuerier,
                         AccessQuerier accessQuerier) {
        this.graphQuerier = graphQuerier;
        this.prohibitionsQuerier = prohibitionsQuerier;
        this.obligationsQuerier = obligationsQuerier;
        this.operationsQuerier = operationsQuerier;
        this.routinesQuerier = routinesQuerier;
        this.accessQuerier = accessQuerier;
    }

    @Override
    public AccessQuerier access() {
        return accessQuerier;
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

    @Override
    public RoutinesQuerier routines() {
        return routinesQuerier;
    }
}