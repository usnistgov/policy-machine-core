package gov.nist.ngac.pm.core.common.exception;

public class PMLConstantNotDefinedException extends PMException {
    public PMLConstantNotDefinedException(String c) {
        super("PML constant " + c + " not defined");
    }
}
