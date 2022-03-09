package gov.nist.csd.pm.policy.exceptions;

public class InvalidProhibitionSubjectException extends PMException{
    public InvalidProhibitionSubjectException(String type) {
        super("invalid prohibition subject type \"" + type + "\"");
    }
}
