package gov.nist.csd.pm.core.common.exception;

public class AdminAccessRightExistsException extends PMException {
    public AdminAccessRightExistsException(String ar) {
        super(String.format("%s is already defined as an admin access right", ar));
    }
}
