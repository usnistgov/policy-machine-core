package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.common.exception.PMException;

public class RoutineExistsException extends PMException {
    public RoutineExistsException(String routine) {
        super("a routine with name " + routine + " already exists");
    }
}
