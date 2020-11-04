package gov.nist.csd.pm.pip.tx;

import gov.nist.csd.pm.exceptions.PMException;

public interface Tx {

    void lock();

    void unlock();

    void runTx(TxRunner txRunner) throws PMException;

}
