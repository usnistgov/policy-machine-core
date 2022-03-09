package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.tx.Transactional;

class TxHandler<T> implements Transactional {

    private T state;
    private boolean inTx;
    private int txCounter;

    public TxHandler() { }

    public void setState(T state) {
        this.state = state;
    }

    public T getState() {
        return state;
    }

    public void setInTx(boolean inTx) {
        this.inTx = inTx;
    }

    public boolean isInTx() {
        return inTx;
    }

    public boolean isTxDoneOnCommit() {
        return txCounter == 1;
    }

    @Override
    public void beginTx() {
        inTx = true;
        txCounter++;
    }

    @Override
    public void commit() {
        txCounter--;
        inTx = txCounter != 0;
        if (!inTx) {
            state = null;
        }
    }

    @Override
    public void rollback() {
        inTx = false;
        txCounter = 0;
        state = null;
    }
}
