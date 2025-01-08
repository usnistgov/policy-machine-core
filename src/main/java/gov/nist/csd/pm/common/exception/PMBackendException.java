package gov.nist.csd.pm.common.exception;

/**
 * Wrapper for backend related exceptions independent of backend implementation.
 */
public class PMBackendException extends PMException {
    public PMBackendException(Exception e) {
        super(e);
    }

    public PMBackendException(String message, Exception e) {
        super(message, e);
    }

    public PMBackendException(String s) {
        super(s);
    }
}
