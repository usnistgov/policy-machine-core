package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class PMLFunctionNotDefinedException extends PMException {


    public PMLFunctionNotDefinedException(String functionName) {
        super("PML function " + functionName + " not defined");
    }
}
