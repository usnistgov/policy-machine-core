package gov.nist.csd.pm.core.common.exception;

public class RoutineDoesNotExistException extends PMException {
    public RoutineDoesNotExistException(String routine) {
        super("a routine with the name " + routine + " does not exist");
    }
}
