package gov.nist.csd.pm.pap.exception;

public class ProhibitionExistsException extends PMException {
    public ProhibitionExistsException(String name) {
        super("a prohibition with the name " + name + " already exists");
    }
}
