package gov.nist.csd.pm.policy.tx;

import gov.nist.csd.pm.policy.exceptions.PMException;

public class TxCommitException extends PMException {
    public TxCommitException(Class target, Class tx) {
        super(target.getName() + " cannot commit from an instance of " + tx.getName());
    }
}
