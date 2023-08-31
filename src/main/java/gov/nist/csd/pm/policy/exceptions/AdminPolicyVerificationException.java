package gov.nist.csd.pm.policy.exceptions;

public class AdminPolicyVerificationException extends PMException{
    public AdminPolicyVerificationException(PMException e) {
        super("error verifying admin policy node", e);
    }
}
