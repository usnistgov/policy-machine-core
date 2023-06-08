package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.io.Serializable;

public class RollbackTxEvent implements PolicySync, PolicyEvent, Serializable {

    private final PolicySync policySync;

    public RollbackTxEvent(PolicySync policySync) {
        this.policySync = policySync;
    }

    @Override
    public PolicySynchronizationEvent policySync() throws PMException {
        return policySync.policySync();
    }

    @Override
    public String getEventName() {
        return "rollback_tx";
    }

    @Override
    public void apply(Policy policy) throws PMException {

    }
}
