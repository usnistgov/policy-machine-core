package gov.nist.csd.pm.policy.exceptions;

import java.security.spec.ECField;
import java.sql.SQLException;
import java.util.Stack;

/**
 * Root exception to cover any possible error in the Policy Machine interfaces. This is to support multiple implementations
 * of the same interfaces that may have different and unplanned exceptions. These exceptions can be wrapped in a PMException.
 */
public class PMException extends Exception {
    private static final long serialVersionUID = 1L;

    public PMException(String msg) {
        super(msg);
    }

    public PMException(Exception e) {
        super(e);
    }

    public PMException(String message, Exception e) {
        super(message, e);
    }

}
