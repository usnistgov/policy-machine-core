package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;

public class MemoryPolicyStoreEventHandler extends BasePolicyEventHandler {

    public MemoryPolicyStoreEventHandler(MemoryPolicyStore store) {
        super(store);
    }

    @Override
    public synchronized void handlePolicyEvent(PolicyEvent event) throws PMException {
        if (event instanceof PolicySynchronizationEvent policySynchronizationEvent) {
            policy = new MemoryPolicyStore(policySynchronizationEvent);
        } else {
            super.handlePolicyEvent(event);
        }
    }
}
