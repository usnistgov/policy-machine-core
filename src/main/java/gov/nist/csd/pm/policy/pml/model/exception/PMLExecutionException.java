package gov.nist.csd.pm.policy.pml.model.exception;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;

public class PMLExecutionException extends PMException {
    public PMLExecutionException(PMLStatement stmt, Exception e) {
        super("error executing: " + stmt.toString(), e);
    }

    public PMLExecutionException(String msg) {
        super(msg);
    }

    public PMLExecutionException(Exception e) {
        super(e);
    }
}
