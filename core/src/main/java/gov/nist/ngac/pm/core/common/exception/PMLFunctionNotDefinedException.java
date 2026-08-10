package gov.nist.ngac.pm.core.common.exception;

public class PMLFunctionNotDefinedException extends PMException {


    public PMLFunctionNotDefinedException(String functionName) {
        super("PML function " + functionName + " not defined");
    }
}
