package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.store.PolicyStore;

public abstract class Querier {

    protected PolicyStore store;

    public Querier(PolicyStore store) {
        this.store = store;
    }

    public Querier(Querier querier) {
        this(querier.store);
    }
}
