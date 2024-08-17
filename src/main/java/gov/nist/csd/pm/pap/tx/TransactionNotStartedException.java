package gov.nist.csd.pm.pap.tx;

import gov.nist.csd.pm.pap.exception.PMException;

public class TransactionNotStartedException extends PMException {
    public TransactionNotStartedException() {
        super("a transaction has not been started using beginTx()");
    }
}
