package gov.nist.ngac.pm.core.common.exception;

/**
 * Base exception for policy machine related exceptions.
 */
public class PMException extends Exception {

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
