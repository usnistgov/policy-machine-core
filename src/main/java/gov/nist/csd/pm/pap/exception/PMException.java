package gov.nist.csd.pm.pap.exception;

/**
 * Root exception to cover any possible error in the Policy Machine interfaces. This is to support multiple implementations
 * of the same interfaces that may have different and unplanned exceptions. These exceptions can be wrapped in a PMException.
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
