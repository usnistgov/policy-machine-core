package gov.nist.csd.pm.policy.exceptions;

public class ConstantAlreadyDefinedException extends PMException {
    public ConstantAlreadyDefinedException(String name) {
        super("constant \"" + name + "\" is already defined");
    }
}
