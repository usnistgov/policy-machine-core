package gov.nist.csd.pm.core.epp;

import gov.nist.csd.pm.core.common.exception.PMException;

public class EPPResponseExecutionException extends PMException {

    public EPPResponseExecutionException(String msg) {
        super(msg);
    }

    public EPPResponseExecutionException(Exception e) {
        super(e);
    }

    public EPPResponseExecutionException(String message, Exception e) {
        super(message, e);
    }
}
