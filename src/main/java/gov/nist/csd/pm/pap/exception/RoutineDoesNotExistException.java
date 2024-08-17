package gov.nist.csd.pm.pap.exception;

public class RoutineDoesNotExistException extends PMException {
    public RoutineDoesNotExistException(String routine) {
        super("a routine with the name " + routine + " does not exist");
    }
}
