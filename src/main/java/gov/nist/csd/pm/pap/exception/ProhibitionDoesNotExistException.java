package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class ProhibitionDoesNotExistException extends PMException {
    public ProhibitionDoesNotExistException(String name) {
        super("prohibition with the name " + name + " does not exist");
    }
}
