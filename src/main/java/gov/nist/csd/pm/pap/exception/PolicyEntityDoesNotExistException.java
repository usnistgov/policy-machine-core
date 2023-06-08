package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class PolicyEntityDoesNotExistException extends PMException {
    public PolicyEntityDoesNotExistException(String msg) {
        super(msg);
    }
}
