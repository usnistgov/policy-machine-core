package gov.nist.csd.pm.pap.exception;

public class OperationDoesNotExistException extends PMException {
    public OperationDoesNotExistException(String op) {
        super("operation " + op + " does not exist");
    }
}
