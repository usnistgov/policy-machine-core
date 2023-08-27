package gov.nist.csd.pm.policy.exceptions;

public class ProhibitionExistsException extends PMException{
    public ProhibitionExistsException(String id) {
        super("a prohibition with the id " + id + " already exists");
    }
}
