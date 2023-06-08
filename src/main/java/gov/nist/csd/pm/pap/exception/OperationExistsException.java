package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class OperationExistsException extends PMException {
    public OperationExistsException(String op) {
        super("operation " + op + " already exists");
    }
}
