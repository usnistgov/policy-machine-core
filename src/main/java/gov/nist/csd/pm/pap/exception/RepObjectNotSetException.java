package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class RepObjectNotSetException extends PMException {

    public RepObjectNotSetException(String node) {
        super("the rep object has not been set for " + node);
    }
}
