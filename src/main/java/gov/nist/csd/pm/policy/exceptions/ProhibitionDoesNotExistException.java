package gov.nist.csd.pm.policy.exceptions;

public class ProhibitionDoesNotExistException extends PMException{
    public ProhibitionDoesNotExistException(String id) {
        super("prohibition with the id " + id + " does not exist");
    }
}
