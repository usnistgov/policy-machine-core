package gov.nist.csd.pm.common.exception;

public class ProhibitionSubjectDoesNotExistException extends PMException {
    public ProhibitionSubjectDoesNotExistException(String name) {
        super("prohibition subject \"" + name + "\" does not exist");
    }
}
