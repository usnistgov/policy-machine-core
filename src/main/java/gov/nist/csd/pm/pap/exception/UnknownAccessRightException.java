package gov.nist.csd.pm.pap.exception;

public class UnknownAccessRightException extends PMException {
    public UnknownAccessRightException(String ar) {
        super("unknown access right \"" + ar + "\"");
    }
}
