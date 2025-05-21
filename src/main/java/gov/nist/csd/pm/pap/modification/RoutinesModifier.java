package gov.nist.csd.pm.pap.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.exception.RoutineExistsException;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.pap.store.PolicyStore;

public class RoutinesModifier extends Modifier implements RoutinesModification {

    public RoutinesModifier(PolicyStore store) {
        super(store);
    }

    @Override
    public void createAdminRoutine(Routine<?, ?> routine) throws PMException {
        if (policyStore.routines().getAdminRoutineNames().contains(routine.getName())) {
            throw new RoutineExistsException(routine.getName());
        }

        policyStore.routines().createAdminRoutine(routine);
    }

    @Override
    public void deleteAdminRoutine(String name) throws PMException {
        if (!policyStore.routines().getAdminRoutineNames().contains(name)) {
            return;
        }

        policyStore.routines().deleteAdminRoutine(name);
    }
}