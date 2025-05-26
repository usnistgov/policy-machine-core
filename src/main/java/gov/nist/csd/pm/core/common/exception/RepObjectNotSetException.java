package gov.nist.csd.pm.core.common.exception;

public class RepObjectNotSetException extends PMException {

    public RepObjectNotSetException(String node) {
        super("the rep object has not been set for " + node);
    }
}
