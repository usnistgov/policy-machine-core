package gov.nist.ngac.pm.core.common.exception;

public class UnknownAccessRightException extends PMException {
    public UnknownAccessRightException(String ar) {
        super("unknown access right \"" + ar + "\"");
    }
}
