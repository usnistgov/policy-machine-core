package gov.nist.csd.pm.common.exception;

public class ProhibitionSubjectDoesNotExistException extends PMException {
    public ProhibitionSubjectDoesNotExistException(long id) {
        super("prohibition subject \"" + id + "\" does not exist");
    }
}
