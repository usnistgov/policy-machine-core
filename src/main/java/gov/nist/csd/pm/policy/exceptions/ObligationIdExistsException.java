package gov.nist.csd.pm.policy.exceptions;

public class ObligationIdExistsException extends PMException{
    public ObligationIdExistsException(String name) {
        super("obligation with name " + name + " already exists");
    }
}
