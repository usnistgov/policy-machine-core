package gov.nist.csd.pm.pap.exception;

public class BootstrapExistingPolicyException extends PMException {
    public BootstrapExistingPolicyException() {
        super("cannot bootstrap when a policy already exists");
    }
}