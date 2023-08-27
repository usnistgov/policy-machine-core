package gov.nist.csd.pm.policy.exceptions;

public class ObligationIdExistsException extends PMException{
    public ObligationIdExistsException(String id) {
        super("obligation with id " + id + " already exists");
    }
}
