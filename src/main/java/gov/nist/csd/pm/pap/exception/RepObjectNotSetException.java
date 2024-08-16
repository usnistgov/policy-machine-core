package gov.nist.csd.pm.pap.exception;

public class RepObjectNotSetException extends PMException {

    public RepObjectNotSetException(String node) {
        super("the rep object has not been set for " + node);
    }
}
