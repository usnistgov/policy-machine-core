package gov.nist.csd.pm.pap.exception;

public class OperationExistsException extends PMException {
    public OperationExistsException(String op) {
        super("operation " + op + " already exists");
    }
}
