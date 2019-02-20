package gov.nist.csd.pm.exceptions;

public class PMException extends Exception {
    private static final long serialVersionUID = 1L;

    private String message;

    public PMException(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
