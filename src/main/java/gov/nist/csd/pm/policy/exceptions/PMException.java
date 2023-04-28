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

    private String message;

    private StackTraceElement[] stackTrace;

    public PMException(String msg, StackTraceElement ... stackTraceElements) {
        this.message = msg;
        this.stackTrace = stackTraceElements;
    }

    public PMException(Exception e) {
        this.message = e.getMessage();
        this.stackTrace = e.getStackTrace();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }
}
