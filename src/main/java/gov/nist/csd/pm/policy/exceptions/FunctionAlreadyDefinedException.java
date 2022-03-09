package gov.nist.csd.pm.policy.exceptions;

public class FunctionAlreadyDefinedException extends PMException {
    public FunctionAlreadyDefinedException(String functionName) {
        super("a function with the name \"" + functionName + "\" is already defined");
    }
}
