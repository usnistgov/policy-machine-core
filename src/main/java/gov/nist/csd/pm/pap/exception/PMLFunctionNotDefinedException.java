package gov.nist.csd.pm.pap.exception;

public class PMLFunctionNotDefinedException extends PMException {


    public PMLFunctionNotDefinedException(String functionName) {
        super("PML function " + functionName + " not defined");
    }
}
