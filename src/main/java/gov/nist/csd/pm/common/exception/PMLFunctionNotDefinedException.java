package gov.nist.csd.pm.common.exception;

public class PMLFunctionNotDefinedException extends PMException {


    public PMLFunctionNotDefinedException(String functionName) {
        super("PML function " + functionName + " not defined");
    }
}
