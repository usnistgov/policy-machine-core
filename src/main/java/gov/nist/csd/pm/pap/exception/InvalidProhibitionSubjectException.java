package gov.nist.csd.pm.pap.exception;

public class InvalidProhibitionSubjectException extends PMException {
    public InvalidProhibitionSubjectException(String type) {
        super("invalid prohibition subject type \"" + type + "\"");
    }
}
