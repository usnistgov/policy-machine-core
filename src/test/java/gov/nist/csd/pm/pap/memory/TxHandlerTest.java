package gov.nist.csd.pm.pap.memory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TxHandlerTest {

    @Test
    void test() {
        TxHandler<String> txHandler = new TxHandler<>();
        txHandler.beginTx();
        assertTrue(txHandler.isTxDoneOnCommit());
        assertTrue(txHandler.isInTx());
        txHandler.commit();
        assertFalse(txHandler.isTxDoneOnCommit());

        txHandler.beginTx();
        txHandler.beginTx();
        assertTrue(txHandler.isInTx());
        assertFalse(txHandler.isTxDoneOnCommit());
        txHandler.commit();
        assertTrue(txHandler.isInTx());
        assertTrue(txHandler.isTxDoneOnCommit());
        txHandler.commit();

        txHandler.beginTx();
        txHandler.beginTx();
        txHandler.rollback();
        assertFalse(txHandler.isInTx());
        assertFalse(txHandler.isTxDoneOnCommit());
    }
}