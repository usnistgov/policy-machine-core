package gov.nist.csd.pm.core.common.exception;

public class InvalidProhibitionSubjectException extends PMException {
    public InvalidProhibitionSubjectException(String type) {
        super("invalid prohibition subject type \"" + type + "\"");
    }
}
