package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.tx.Transactional;

import java.util.Objects;

public class MemoryTx implements Transactional {
    private boolean active;
    private int counter;

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
        active = true;
        counter++;
    }

    @Override
    public void commit() {
        counter--;
        active = counter != 0;
    }

    @Override
    public void rollback() {
        counter = 0;
        active = false;
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
