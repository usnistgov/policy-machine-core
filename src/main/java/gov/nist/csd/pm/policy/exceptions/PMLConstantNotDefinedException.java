package gov.nist.csd.pm.policy.exceptions;

public class PMLConstantNotDefinedException extends PMException{
    public PMLConstantNotDefinedException(String c) {
        super("PML constant " + c + " not defined");
    }
}
