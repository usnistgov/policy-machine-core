package gov.nist.csd.pm.core.impl.memory.pap.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class MemoryTxTest {

    @Test
    void testNestedTxOnSameThread() {
        MemoryTx tx = new MemoryTx();

        tx.beginTx();
        tx.beginTx();
        assertTrue(tx.isActive());
        assertEquals(2, tx.getCounter());

        tx.commit();
        assertTrue(tx.isActive());
        assertEquals(1, tx.getCounter());

        tx.commit();
        assertFalse(tx.isActive());
        assertEquals(0, tx.getCounter());
    }

    @Test
    void testBeginTxFromDifferentThreadThrows() throws InterruptedException {
        MemoryTx tx = new MemoryTx();
        tx.beginTx();

        AtomicReference<Throwable> thrown = new AtomicReference<>();
        Thread other = new Thread(() -> {
            try {
                tx.beginTx();
            } catch (Throwable t) {
                thrown.set(t);
            }
        });
        other.start();
        other.join();

        assertTrue(thrown.get() instanceof ConcurrentTxException);
    }

    @Test
    void testCommitFromDifferentThreadThrows() throws InterruptedException {
        MemoryTx tx = new MemoryTx();
        tx.beginTx();

        AtomicReference<Throwable> thrown = new AtomicReference<>();
        Thread other = new Thread(() -> {
            try {
                tx.commit();
            } catch (Throwable t) {
                thrown.set(t);
            }
        });
        other.start();
        other.join();

        assertTrue(thrown.get() instanceof ConcurrentTxException);
    }

    @Test
    void testRollbackFromDifferentThreadThrows() throws InterruptedException {
        MemoryTx tx = new MemoryTx();
        tx.beginTx();

        AtomicReference<Throwable> thrown = new AtomicReference<>();
        Thread other = new Thread(() -> {
            try {
                tx.rollback();
            } catch (Throwable t) {
                thrown.set(t);
            }
        });
        other.start();
        other.join();

        assertTrue(thrown.get() instanceof ConcurrentTxException);
    }

    @Test
    void testDifferentThreadCanBeginAfterOwnerEndsTx() throws InterruptedException {
        MemoryTx tx = new MemoryTx();
        tx.beginTx();
        tx.commit();

        AtomicReference<Throwable> thrown = new AtomicReference<>();
        Thread other = new Thread(() -> {
            try {
                tx.beginTx();
            } catch (Throwable t) {
                thrown.set(t);
            }
        });
        other.start();
        other.join();

        assertEquals(null, thrown.get());
        assertTrue(tx.isActive());
    }

}
