package gov.nist.csd.pm.common.exception;

public class ProhibitionContainerDoesNotExistException extends PMException {
    public ProhibitionContainerDoesNotExistException(String name) {
        super("prohibition container \"" + name + "\" does not exist");
    }
}
