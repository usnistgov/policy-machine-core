package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.pap.store.PolicyStore;

abstract class Querier {

    protected PolicyStore store;

    public Querier(PolicyStore store) {
        this.store = store;
    }
}
