package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class PMLConstantNotDefinedException extends PMException {
    public PMLConstantNotDefinedException(String c) {
        super("PML constant " + c + " not defined");
    }
}
