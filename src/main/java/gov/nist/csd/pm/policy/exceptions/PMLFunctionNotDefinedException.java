package gov.nist.csd.pm.policy.exceptions;

public class PMLFunctionNotDefinedException extends PMException{


    public PMLFunctionNotDefinedException(String functionName) {
        super("PML function " + functionName + " not defined");
    }
}
