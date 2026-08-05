package gov.nist.ngac.pm.core.pap.pml.exception;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementSerializable;

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
