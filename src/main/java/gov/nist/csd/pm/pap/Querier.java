package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.store.PolicyStore;

public abstract class Querier {

    protected PolicyStore store;

    public Querier(PolicyStore store) {
        this.store = store;
    }
}
