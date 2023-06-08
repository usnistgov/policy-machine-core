package gov.nist.csd.pm.impl.memory.pap.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TxCmdTracker {

    private final List<TxCmd> operations;

    public TxCmdTracker() {
        operations = new ArrayList<>();
    }

    public List<TxCmd> getOperations() {
        List<TxCmd> copy = new ArrayList<>(operations);

        Collections.reverse(copy);

        return copy;
    }

    public void trackOp(MemoryTx tx, TxCmd op) {
        if (!tx.isActive()) {
            return;
        }

        this.operations.add(op);
    }

    public void clearOps() {
        this.operations.clear();
    }
}
