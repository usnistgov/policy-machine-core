package gov.nist.csd.pm.policy.exceptions;

public class NoParentException extends PMException{
    public NoParentException() {
        super("a null or empty parent value was provided");
    }
}
