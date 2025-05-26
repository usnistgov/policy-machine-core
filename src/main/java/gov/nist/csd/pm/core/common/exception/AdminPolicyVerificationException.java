package gov.nist.csd.pm.core.common.exception;

public class AdminPolicyVerificationException extends PMException {
    public AdminPolicyVerificationException(PMException e) {
        super("error verifying admin policy node", e);
    }
}
