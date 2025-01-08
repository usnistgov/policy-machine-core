package gov.nist.csd.pm.common.exception;

public class ObligationNameExistsException extends PMException {
    public ObligationNameExistsException(String name) {
        super("obligation with name " + name + " already exists");
    }
}
