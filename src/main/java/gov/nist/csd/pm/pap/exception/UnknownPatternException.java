package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class UnknownPatternException extends PMException {
    public UnknownPatternException() {
        super("unknown pattern");
    }
}
