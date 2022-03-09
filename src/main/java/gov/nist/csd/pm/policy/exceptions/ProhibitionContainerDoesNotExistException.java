package gov.nist.csd.pm.policy.exceptions;

public class ProhibitionContainerDoesNotExistException extends PMException{
    public ProhibitionContainerDoesNotExistException(String name) {
        super("prohibition container \"" + name + "\" does not exist");
    }
}
