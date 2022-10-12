package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.exceptions.PMException;

public class RollbackTxEvent implements PolicySync, PolicyEvent {

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
}
