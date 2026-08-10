package gov.nist.ngac.pm.core.common.tx;

import gov.nist.ngac.pm.core.common.exception.PMException;

public class TransactionNotStartedException extends PMException {
    public TransactionNotStartedException() {
        super("a transaction has not been started using beginTx()");
    }
}
