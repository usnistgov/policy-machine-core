package gov.nist.csd.pm.policy.exceptions;

public class ProhibitionExistsException extends PMException{
    public ProhibitionExistsException(String label) {
        super("a prohibition with the label " + label + " already exists");
    }
}
