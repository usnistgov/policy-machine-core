package gov.nist.csd.pm.pap.exception;

public class CannotDeleteAdminPolicyConfigException extends PMException {
    public CannotDeleteAdminPolicyConfigException() {
        super("cannot delete PM_ADMIN_PC, PM_ADMIN_OBJECT, or the assignment between the two");
    }
}
