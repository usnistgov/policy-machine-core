package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.exception.RoutineExistsException;
import gov.nist.csd.pm.pap.modification.RoutinesModification;
import gov.nist.csd.pm.pap.routine.Routine;
import gov.nist.csd.pm.pap.store.PolicyStore;

public class RoutinesModifier extends Modifier implements RoutinesModification {

    public RoutinesModifier(PolicyStore store) {
        super(store);
    }

    @Override
    public void createAdminRoutine(Routine<?> routine) throws PMException {
        if (store.routines().getAdminRoutineNames().contains(routine.getName())) {
            throw new RoutineExistsException(routine.getName());
        }

        store.routines().createAdminRoutine(routine);
    }

    @Override
    public void deleteAdminRoutine(String name) throws PMException {
        if (!store.routines().getAdminRoutineNames().contains(name)) {
            return;
        }

        store.routines().deleteAdminRoutine(name);
    }
}