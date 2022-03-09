package gov.nist.csd.pm.policy.exceptions;

public class ProhibitionSubjectDoesNotExistException extends PMException{
    public ProhibitionSubjectDoesNotExistException(String name) {
        super("prohibition subject \"" + name + "\" does not exist");
    }
}
