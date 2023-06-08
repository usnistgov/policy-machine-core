package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.pap.store.PolicyStore;

public abstract class PolicyQuerier extends Querier implements PolicyQuery {

    private final GraphQuerier graphQuerier;
    private final ProhibitionsQuerier prohibitionsQuerier;
    private final ObligationsQuerier obligationsQuerier;
    private final OperationsQuerier operationsQuerier;
    private final RoutinesQuerier routinesQuerier;

    public PolicyQuerier(PolicyStore store) {
        super(store);

        this.graphQuerier = new GraphQuerier(store);
        this.prohibitionsQuerier = new ProhibitionsQuerier(store, graphQuerier);
        this.obligationsQuerier = new ObligationsQuerier(store);
        this.operationsQuerier = new OperationsQuerier(store);
        this.routinesQuerier = new RoutinesQuerier(store);
    }

    public PolicyQuerier(Querier querier) {
        this(querier.store);
    }

    @Override
    public abstract AccessQuerier access();

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