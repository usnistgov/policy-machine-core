package gov.nist.csd.pm.common.exception;

public class OperationExistsException extends PMException {
    public OperationExistsException(String op) {
        super("operation " + op + " already exists");
    }
}
