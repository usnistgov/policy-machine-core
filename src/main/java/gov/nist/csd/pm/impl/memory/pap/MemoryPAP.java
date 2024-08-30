package gov.nist.csd.pm.impl.memory.pap;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.store.MemoryPolicyStore;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PolicyQuerier;

public class MemoryPAP extends PAP {

    private final MemoryPolicyQuerier querier;

    public MemoryPAP() throws PMException {
        super(new MemoryPolicyStore());
        this.querier = new MemoryPolicyQuerier(policyStore());
    }

    public MemoryPAP(MemoryPolicyStore store) throws PMException {
        super(store);
        this.querier = new MemoryPolicyQuerier(policyStore());
    }

    public MemoryPAP(PAP pap) throws PMException {
        super(pap);
        this.querier = new MemoryPolicyQuerier(policyStore());
    }

    @Override
    public PolicyQuerier query() {
        return querier;
    }

    @Override
    public void beginTx() throws PMException {
        policyStore.beginTx();
    }

    @Override
    public void commit() throws PMException {
        policyStore.commit();
    }

    @Override
    public void rollback() throws PMException {
        policyStore.rollback();
    }
}
