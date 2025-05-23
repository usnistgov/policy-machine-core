package gov.nist.csd.pm.core.common.exception;

public class ProhibitionContainerDoesNotExistException extends PMException {
    public ProhibitionContainerDoesNotExistException(long id) {
        super("prohibition container \"" + id + "\" does not exist");
    }
}
