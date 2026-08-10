package gov.nist.ngac.pm.core.common.exception;

public class CannotDeleteAdminPolicyConfigException extends PMException {
    public CannotDeleteAdminPolicyConfigException() {
        super("cannot delete nodes or assignments in AdminPolicy");
    }
}
