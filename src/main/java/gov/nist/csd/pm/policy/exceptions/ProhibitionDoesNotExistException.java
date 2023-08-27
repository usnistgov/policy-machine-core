package gov.nist.csd.pm.policy.exceptions;

public class ProhibitionDoesNotExistException extends PMException{
    public ProhibitionDoesNotExistException(String name) {
        super("prohibition with the name " + name + " does not exist");
    }
}
