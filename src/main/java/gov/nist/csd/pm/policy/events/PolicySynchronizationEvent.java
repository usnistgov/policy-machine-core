package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;

public class PolicySynchronizationEvent implements PolicyEvent {

    private final MemoryPolicyStore policyStore;

    public PolicySynchronizationEvent(MemoryPolicyStore policyStore) {
        this.policyStore = policyStore;
    }

    public MemoryPolicyStore getPolicyStore() {
        return policyStore;
    }

    @Override
    public String getEventName() {
        return "policy_sync";
    }
}
