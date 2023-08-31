package gov.nist.csd.pm.policy.exceptions;

public class BootstrapExistingPolicyException extends PMException {
    public BootstrapExistingPolicyException() {
        super("cannot bootstrap when a policy already exists");
    }
}
