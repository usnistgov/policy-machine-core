package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class BootstrapExistingPolicyException extends PMException {
    public BootstrapExistingPolicyException() {
        super("cannot bootstrap when a policy already exists");
    }
}
