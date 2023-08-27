package gov.nist.csd.pm.policy.exceptions;

public class PMRuntimeException extends RuntimeException {

    public PMRuntimeException(String message) {
        super(message);
    }

    public PMRuntimeException(Exception e) {
        super(e);
    }
}
