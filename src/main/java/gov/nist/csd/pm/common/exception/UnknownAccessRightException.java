package gov.nist.csd.pm.common.exception;

public class UnknownAccessRightException extends PMException {
    public UnknownAccessRightException(String ar) {
        super("unknown access right \"" + ar + "\"");
    }
}
