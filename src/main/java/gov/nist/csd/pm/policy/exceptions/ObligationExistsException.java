package gov.nist.csd.pm.policy.exceptions;

public class ObligationExistsException extends PMException{
    public ObligationExistsException(String label) {
        super("obligation with label " + label + " already exists");
    }
}
