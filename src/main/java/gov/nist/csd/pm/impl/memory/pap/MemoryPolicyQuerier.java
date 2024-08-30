package gov.nist.csd.pm.impl.memory.pap;

import gov.nist.csd.pm.pap.PolicyQuerier;
import gov.nist.csd.pm.pap.store.PolicyStore;

public class MemoryPolicyQuerier extends PolicyQuerier {

    private final MemoryAccessQuerier accessQuerier;

    public MemoryPolicyQuerier(PolicyStore policyStore) {
        super(policyStore);
        this.accessQuerier = new MemoryAccessQuerier(policyStore, graph(), prohibitions());
    }

    @Override
    public MemoryAccessQuerier access() {
        return accessQuerier;
    }
}
