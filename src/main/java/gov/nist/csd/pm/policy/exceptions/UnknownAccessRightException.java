package gov.nist.csd.pm.policy.exceptions;

public class UnknownAccessRightException extends PMException{
    public UnknownAccessRightException(String ar) {
        super("unknown access right \"" + ar + "\"");
    }
}
