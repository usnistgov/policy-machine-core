package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.pap.store.PolicyStore;

abstract class Querier {

    protected PolicyStore store;

    public Querier(PolicyStore store) {
        this.store = store;
    }
}
