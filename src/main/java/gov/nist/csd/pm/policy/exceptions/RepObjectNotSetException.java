package gov.nist.csd.pm.policy.exceptions;

public class RepObjectNotSetException extends PMException{

    public RepObjectNotSetException(String node) {
        super("the rep object has not been set for " + node);
    }
}
