package gov.nist.csd.pm.common.exception;

public class PMRuntimeException extends RuntimeException {

    public PMRuntimeException(String message) {
        super(message);
    }

    public PMRuntimeException(Exception e) {
        super(e);
    }

    public PMRuntimeException(String message, Exception e) {
        super(message, e);
    }

    public PMRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PMRuntimeException(Throwable cause) {
        super(cause);
    }

    public PMRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
