package gov.nist.csd.pm.policy.exceptions;

public class ObligationDoesNotExistException extends PMException{
    public ObligationDoesNotExistException(String label) {
        super("obligation with label " + label + " does not exist");
    }
}
