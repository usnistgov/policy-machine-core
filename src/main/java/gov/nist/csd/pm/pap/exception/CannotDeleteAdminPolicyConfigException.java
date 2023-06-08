package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class CannotDeleteAdminPolicyConfigException extends PMException {
    public CannotDeleteAdminPolicyConfigException() {
        super("cannot delete ADMIN_POLICY, ADMIN_POLICY_OBJECT, or the assignment between the two");
    }
}
