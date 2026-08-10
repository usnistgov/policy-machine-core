package gov.nist.ngac.pm.core.common.exception;

public class BootstrapExistingPolicyException extends PMException {
    public BootstrapExistingPolicyException() {
        super("cannot bootstrap when a policy already exists");
    }
}
