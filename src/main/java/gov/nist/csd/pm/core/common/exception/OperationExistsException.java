package gov.nist.csd.pm.core.common.exception;

public class OperationExistsException extends PMException {
    public OperationExistsException(String op) {
        super("a function with the name '" + op + "' already exists");
    }
}
