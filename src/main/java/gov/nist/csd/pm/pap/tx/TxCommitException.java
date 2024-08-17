package gov.nist.csd.pm.pap.tx;

import gov.nist.csd.pm.pap.exception.PMException;

public class TxCommitException extends PMException {
    public TxCommitException(Class target, Class tx) {
        super(target.getName() + " cannot commit from an instance of " + tx.getName());
    }
}
