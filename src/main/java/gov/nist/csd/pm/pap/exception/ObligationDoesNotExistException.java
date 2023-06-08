package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class ObligationDoesNotExistException extends PMException {
    public ObligationDoesNotExistException(String name) {
        super("obligation with name " + name + " does not exist");
    }
}
