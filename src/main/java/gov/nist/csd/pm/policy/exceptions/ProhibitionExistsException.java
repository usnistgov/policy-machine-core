package gov.nist.csd.pm.policy.exceptions;

public class ProhibitionExistsException extends PMException{
    public ProhibitionExistsException(String name) {
        super("a prohibition with the name " + name + " already exists");
    }
}
