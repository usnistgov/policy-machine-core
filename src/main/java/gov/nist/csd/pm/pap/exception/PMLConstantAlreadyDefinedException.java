package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class PMLConstantAlreadyDefinedException extends PMException {
    public PMLConstantAlreadyDefinedException(String name) {
        super("constant \"" + name + "\" is already defined");
    }
}
