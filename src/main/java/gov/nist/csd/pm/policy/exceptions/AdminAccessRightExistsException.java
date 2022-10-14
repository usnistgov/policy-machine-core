package gov.nist.csd.pm.policy.exceptions;

public class AdminAccessRightExistsException extends PMException{
    public AdminAccessRightExistsException(String ar) {
        super(String.format("%s is already defined as an admin access right", ar));
    }
}
