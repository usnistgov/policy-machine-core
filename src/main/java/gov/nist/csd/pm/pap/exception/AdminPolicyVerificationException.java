package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class AdminPolicyVerificationException extends PMException {
    public AdminPolicyVerificationException(PMException e) {
        super("error verifying admin policy node", e);
    }
}
