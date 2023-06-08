package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.events.PolicyEventListener;
import gov.nist.csd.pm.policy.events.PolicySynchronizationEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;

public class MemoryPolicyStoreEventHandler implements PolicyEventListener {

    private MemoryPolicyStore store;

    public MemoryPolicyStoreEventHandler(MemoryPolicyStore store) {
        this.store = store;
    }

    public void setPolicyStore(MemoryPolicyStore memoryPolicyStore) {
        this.store = memoryPolicyStore;
    }

    @Override
    public void handlePolicyEvent(PolicyEvent event) throws PMException {
        if (event instanceof PolicySynchronizationEvent policySynchronizationEvent) {
            store = policySynchronizationEvent.getPolicyStore();
        } else {
            event.apply(store);
        }
    }
}
