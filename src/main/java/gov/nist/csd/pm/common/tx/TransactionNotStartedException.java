package gov.nist.csd.pm.common.tx;

import gov.nist.csd.pm.common.exception.PMException;

public class TransactionNotStartedException extends PMException {
    public TransactionNotStartedException() {
        super("a transaction has not been started using beginTx()");
    }
}
