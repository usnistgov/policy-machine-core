package gov.nist.csd.pm.pap.exception;

public class CannotDeleteAdminPolicyConfigException extends PMException {
    public CannotDeleteAdminPolicyConfigException() {
        super("cannot delete ADMIN_POLICY, ADMIN_POLICY_OBJECT, or the assignment between the two");
    }
}
