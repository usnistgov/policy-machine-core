package gov.nist.csd.pm.pap.exception;

public class PMLConstantNotDefinedException extends PMException {
    public PMLConstantNotDefinedException(String c) {
        super("PML constant " + c + " not defined");
    }
}
