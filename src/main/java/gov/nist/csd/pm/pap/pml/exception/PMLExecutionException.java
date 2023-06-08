package gov.nist.csd.pm.pap.pml.exception;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementSerializable;

public class PMLExecutionException extends PMException {
    public PMLExecutionException(PMLStatementSerializable stmt, Exception e) {
        super("error executing: " + stmt.toString(), e);
    }

    public PMLExecutionException(String msg) {
        super(msg);
    }

    public PMLExecutionException(Exception e) {
        super(e);
    }
}
