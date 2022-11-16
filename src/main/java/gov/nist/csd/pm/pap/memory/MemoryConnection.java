package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.*;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;

class MemoryConnection extends PolicyStoreConnection {

    private final MemoryPolicyStore main;

    public MemoryConnection(MemoryPolicyStore main) {
        this.main = main;
    }

    @Override
    public void beginTx() throws PMException {
        main.beginTx();
    }

    @Override
    public void commit() throws PMException {
        main.commit();
    }

    @Override
    public void rollback() throws PMException {
        main.rollback();
    }

    @Override
    public GraphStore graph() {
        return main.graph();
    }

    @Override
    public ProhibitionsStore prohibitions() {
        return main.prohibitions();
    }

    @Override
    public ObligationsStore obligations() {
        return main.obligations();
    }

    @Override
    public PALStore pal() {
        return main.pal();
    }

    @Override
    public PolicySynchronizationEvent policySync() throws PMException {
        return main.policySync();
    }
}
