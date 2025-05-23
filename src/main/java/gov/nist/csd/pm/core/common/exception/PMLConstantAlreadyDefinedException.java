package gov.nist.csd.pm.core.common.exception;

public class PMLConstantAlreadyDefinedException extends PMException {
    public PMLConstantAlreadyDefinedException(String name) {
        super("constant \"" + name + "\" is already defined");
    }
}
