package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class AdminAccessRightExistsException extends PMException {
    public AdminAccessRightExistsException(String ar) {
        super(String.format("%s is already defined as an admin access right", ar));
    }
}
