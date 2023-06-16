package gov.nist.csd.pm.pap.memory;

import java.util.Objects;

public class MemoryTx {
    private boolean active;
    private int counter;
    private TxPolicyStore policyStore;

    public MemoryTx(boolean active, int counter, TxPolicyStore policyStore) {
        this.active = active;
        this.counter = counter;
        this.policyStore = policyStore;
    }

    public void set(boolean active, int counter, TxPolicyStore policyStore) {
        this.active = active;
        this.counter = counter;
        this.policyStore = policyStore;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public TxPolicyStore getPolicyStore() {
        return policyStore;
    }

    public void setPolicyStore(TxPolicyStore policyStore) {
        this.policyStore = policyStore;
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MemoryTx) obj;
        return this.active == that.active &&
                this.counter == that.counter &&
                Objects.equals(this.policyStore, that.policyStore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, counter, policyStore);
    }

    @Override
    public String toString() {
        return "MemoryTx[" +
                "active=" + active + ", " +
                "counter=" + counter + ", " +
                "policyStore=" + policyStore + ']';
    }

}
