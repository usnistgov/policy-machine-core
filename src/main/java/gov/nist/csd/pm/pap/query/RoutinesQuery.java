package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.routine.Routine;

import java.util.Collection;

/**
 * Interface to query routines.
 */
public interface RoutinesQuery {

    /**
     * Get the names of all admin routines.
     * @return A collection of all the admin routine names.
     * @throws PMException If there is an error in the PM.
     */
    Collection<String> getAdminRoutineNames() throws PMException;

    /**
     * Get the admin operation with the given name.
     * @param routineName The routine name.
     * @return The Routine object.
     * @throws PMException If there is an error in the PM.
     */
    Routine<?> getAdminRoutine(String routineName) throws PMException;

}
