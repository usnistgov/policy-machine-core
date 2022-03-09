package gov.nist.csd.pm.policy.exceptions;

public class UnknownResourceAccessRightException extends PMException{
    public UnknownResourceAccessRightException(String ar) {
        super("unknown resource access right \"" + ar + "\"");
    }
}
