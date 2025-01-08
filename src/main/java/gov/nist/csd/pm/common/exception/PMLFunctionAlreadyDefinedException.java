package gov.nist.csd.pm.common.exception;

public class PMLFunctionAlreadyDefinedException extends PMException {
    public PMLFunctionAlreadyDefinedException(String functionName) {
        super("a function with the name \"" + functionName + "\" is already defined");
    }
}
