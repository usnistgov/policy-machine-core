package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.routine.Routine;

import java.util.Collection;

public interface RoutinesQuery {

    Collection<String> getAdminRoutineNames() throws PMException;
    Routine getAdminRoutine(String routineName) throws PMException;

}
