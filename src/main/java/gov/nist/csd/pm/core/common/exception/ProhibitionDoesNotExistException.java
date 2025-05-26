package gov.nist.csd.pm.core.common.exception;

public class ProhibitionDoesNotExistException extends PMException {
    public ProhibitionDoesNotExistException(String name) {
        super("prohibition with the name " + name + " does not exist");
    }
}
