package gov.nist.csd.pm.pap.exception;

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

}
