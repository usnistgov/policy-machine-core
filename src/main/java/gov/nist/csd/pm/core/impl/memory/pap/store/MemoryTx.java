package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.tx.Transactional;
import java.util.Objects;

/**
 * Tracks the active/nested state of a transaction on a {@link MemoryPolicyStore}. Supports
 * re-entrant (nested) transactions from a single thread via {@link #counter}, but the in-memory
 * store is not designed for concurrent use: {@link #beginTx()}, {@link #commit()}, and
 * {@link #rollback()} record the thread that owns the currently active transaction and throw
 * {@link ConcurrentTxException} if a different thread attempts to participate in it. This turns
 * concurrent misuse into an immediate, loud failure instead of silently corrupting policy state.
 */
public class MemoryTx implements Transactional {
    private boolean active;
    private int counter;
    private Thread owner;

    public MemoryTx() {
        active = false;
        counter = 0;
    }

    public MemoryTx(boolean active, int counter) {
        this.active = active;
        this.counter = counter;
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

    @Override
    public void beginTx() {
        checkOwnerThread();

        if (!active) {
            owner = Thread.currentThread();
        }

        active = true;
        counter++;
    }

    @Override
    public void commit() {
        checkOwnerThread();

        counter--;
        active = counter != 0;

        if (!active) {
            owner = null;
        }
    }

    @Override
    public void rollback() {
        checkOwnerThread();

        counter = 0;
        active = false;
        owner = null;
    }

    private void checkOwnerThread() {
        if (active && owner != null && owner != Thread.currentThread()) {
            throw new ConcurrentTxException(owner, Thread.currentThread());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryTx memoryTx = (MemoryTx) o;
        return active == memoryTx.active && counter == memoryTx.counter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(active, counter);
    }

    @Override
    public String toString() {
        return "MemoryTx{" +
                "active=" + active +
                ", counter=" + counter +
                '}';
    }
}
