package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.tx.Transactional;

import java.util.Objects;

/**
 * @param <T> The BaseMemoryTx instance that will run the tx's for this class.
 */
public class MemoryTx<T extends BaseMemoryTx> implements Transactional {
    private boolean active;
    private int counter;
    private T store;

    public MemoryTx() {
        active = false;
        counter = 0;
        store = null;
    }

    public MemoryTx(boolean active, int counter, T store) {
        this.active = active;
        this.counter = counter;
        this.store = store;
    }

    public void set(boolean active, int counter, T policyStore) {
        this.active = active;
        this.counter = counter;
        this.store = policyStore;
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

    public T getStore() {
        return store;
    }

    public void setStore(T store) {
        this.store = store;
    }

    @Override
    public void beginTx() throws PMException {
        active = true;
        counter++;
    }

    @Override
    public void commit() throws PMException {
        counter--;
        active = counter != 0;
    }

    @Override
    public void rollback() throws PMException {
        counter--;
        active = counter != 0;
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MemoryTx) obj;
        return this.active == that.active &&
                this.counter == that.counter &&
                Objects.equals(this.store, that.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, counter, store);
    }

    @Override
    public String toString() {
        return "MemoryTx[" +
                "active=" + active + ", " +
                "counter=" + counter + ", " +
                "policyStore=" + store + ']';
    }
}
