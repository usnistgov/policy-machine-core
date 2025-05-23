package gov.nist.csd.pm.core.common.exception;

public class ObligationDoesNotExistException extends PMException {
    public ObligationDoesNotExistException(String name) {
        super("obligation with name " + name + " does not exist");
    }
}
