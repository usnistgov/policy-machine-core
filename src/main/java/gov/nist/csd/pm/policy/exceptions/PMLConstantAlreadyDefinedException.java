package gov.nist.csd.pm.policy.exceptions;

public class PMLConstantAlreadyDefinedException extends PMException {
    public PMLConstantAlreadyDefinedException(String name) {
        super("constant \"" + name + "\" is already defined");
    }
}
