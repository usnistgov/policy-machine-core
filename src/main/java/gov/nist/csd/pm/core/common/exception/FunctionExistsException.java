package gov.nist.csd.pm.core.common.exception;

public class FunctionExistsException extends PMException {
    public FunctionExistsException(String op) {
        super("a function with the name '" + op + "' already exists");
    }
}
