package gov.nist.csd.pm.common.exception;

public class RoutineExistsException extends PMException {
    public RoutineExistsException(String routine) {
        super("a routine with name " + routine + " already exists");
    }
}
