package gov.nist.csd.pm.core.common.exception;

public class ProhibitionSubjectDoesNotExistException extends PMException {
    public ProhibitionSubjectDoesNotExistException(long id) {
        super("prohibition subject \"" + id + "\" does not exist");
    }
}
