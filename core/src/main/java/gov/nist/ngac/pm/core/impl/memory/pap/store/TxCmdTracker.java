package gov.nist.ngac.pm.core.impl.memory.pap.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tracks the undo commands issued during an in-memory transaction so they can be replayed on rollback.
 */
public class TxCmdTracker {

    private final List<TxCmd> operations;

    public TxCmdTracker() {
        operations = new ArrayList<>();
    }

    /**
     * Returns the tracked commands in reverse order, for rollback replay.
     *
     * @return the tracked commands, most recent first
     */
    public List<TxCmd> getOperations() {
        List<TxCmd> copy = new ArrayList<>(operations);

        Collections.reverse(copy);

        return copy;
    }

    /**
     * Records an undo command for the given transaction. Does nothing if the transaction is not active.
     *
     * @param tx the transaction the command belongs to
     * @param op the undo command to record
     */
    public void trackOp(MemoryTx tx, TxCmd op) {
        if (!tx.isActive()) {
            return;
        }

        this.operations.add(op);
    }

    /**
     * Discards all tracked commands, e.g. after a commit.
     */
    public void clearOps() {
        this.operations.clear();
    }
}
