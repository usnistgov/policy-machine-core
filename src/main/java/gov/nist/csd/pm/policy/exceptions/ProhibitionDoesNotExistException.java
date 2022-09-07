package gov.nist.csd.pm.policy.exceptions;

public class ProhibitionDoesNotExistException extends PMException{
    public ProhibitionDoesNotExistException(String label) {
        super("prohibition with the label " + label + " does not exist");
    }
}
