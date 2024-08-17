package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.pap.store.PolicyStore;

public abstract class AccessQuerier extends Querier implements AccessQuery{

    public AccessQuerier(PolicyStore store) {
        super(store);
    }

    public AccessQuerier(Querier querier) {
        super(querier);
    }
}