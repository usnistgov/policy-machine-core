/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

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
