package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class InvalidProhibitionSubjectException extends PMException {
    public InvalidProhibitionSubjectException(String type) {
        super("invalid prohibition subject type \"" + type + "\"");
    }
}
