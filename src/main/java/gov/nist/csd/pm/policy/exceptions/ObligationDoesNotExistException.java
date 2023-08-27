package gov.nist.csd.pm.policy.exceptions;

public class ObligationDoesNotExistException extends PMException{
    public ObligationDoesNotExistException(String id) {
        super("obligation with id " + id + " does not exist");
    }
}
