package gov.nist.csd.pm.pap.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.executable.routine.Routine;

/**
 * Interface for managing NGAC administrative routines.
 */
public interface RoutinesModification {

    /**
     * Create a new administrative routine.
     * @param routine The routine to create.
     * @throws PMException If there is an error in the PM.
     */
    void createAdminRoutine(Routine<?> routine) throws PMException;

    /**
     * Delete the admin routine with the given name.
     * @param name The name of the admin routine to delete.
     * @throws PMException If there is an error in the PM.
     */
    void deleteAdminRoutine(String name) throws PMException;

}
