package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class OperationDoesNotExistException extends PMException {
    public OperationDoesNotExistException(String op) {
        super("operation " + op + " does not exist");
    }
}
