package gov.nist.csd.pm.core.impl.memory.pap.store;

/**
 * Thrown when a thread other than the one that started the active transaction attempts to
 * begin, commit, or roll back a transaction on a {@link MemoryPolicyStore}. The in-memory
 * backend is intended for single-threaded use; this is a fail-fast guard against concurrent
 * misuse rather than a concurrency control mechanism.
 */
public class ConcurrentTxException extends IllegalStateException {

    public ConcurrentTxException(Thread owner, Thread attempted) {
        super("transaction owned by thread '" + owner.getName()
                + "' cannot be accessed by thread '" + attempted.getName() + "'; "
                + "the in-memory policy store does not support concurrent transactions");
    }

}
