package gov.nist.ngac.pm.core.impl.memory.pap.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Records the undo commands issued during an in-memory transaction so they can be replayed in reverse
 * on rollback.
 */
public class TxCmdTracker {

    private final List<TxCmd> operations;

    public TxCmdTracker() {
        operations = new ArrayList<>();
    }

    /**
     * Returns the tracked commands in reverse (most-recent-first) order, ready for rollback replay.
     *
     * @return a new list of the tracked commands, reversed
     */
    public List<TxCmd> getOperations() {
        List<TxCmd> copy = new ArrayList<>(operations);

        Collections.reverse(copy);

        return copy;
    }

    /**
     * Records an undo command, unless the given transaction is not active — in which case there is
     * nothing to roll back to and the command is discarded.
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
     * Discards all tracked commands, e.g. after a successful commit.
     */
    public void clearOps() {
        this.operations.clear();
    }
}
