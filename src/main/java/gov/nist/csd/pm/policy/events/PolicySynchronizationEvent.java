package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolicySynchronizationEvent that = (PolicySynchronizationEvent) o;
        return Objects.equals(policyStore, that.policyStore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policyStore);
    }
}
