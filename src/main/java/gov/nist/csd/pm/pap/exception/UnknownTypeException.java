package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class UnknownTypeException extends PMException {
    public UnknownTypeException(String type) {
        super("unknown node type " + type);
    }
}
