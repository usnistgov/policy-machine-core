package gov.nist.csd.pm.policy.exceptions;

public class ObligationDoesNotExistException extends PMException{
    public ObligationDoesNotExistException(String name) {
        super("obligation with name " + name + " does not exist");
    }
}
