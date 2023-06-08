package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class ProhibitionContainerDoesNotExistException extends PMException {
    public ProhibitionContainerDoesNotExistException(String name) {
        super("prohibition container \"" + name + "\" does not exist");
    }
}
