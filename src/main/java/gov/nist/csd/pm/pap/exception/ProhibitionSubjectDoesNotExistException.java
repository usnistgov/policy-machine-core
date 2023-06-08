package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class ProhibitionSubjectDoesNotExistException extends PMException {
    public ProhibitionSubjectDoesNotExistException(String name) {
        super("prohibition subject \"" + name + "\" does not exist");
    }
}
