package gov.nist.csd.pm.core.common.exception;

public class BootstrapExistingPolicyException extends PMException {
    public BootstrapExistingPolicyException() {
        super("cannot bootstrap when a policy already exists");
    }
}
