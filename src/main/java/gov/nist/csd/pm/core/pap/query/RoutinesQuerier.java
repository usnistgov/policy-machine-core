package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.RoutineDoesNotExistException;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.store.PolicyStore;

import java.util.Collection;

public class RoutinesQuerier extends Querier implements RoutinesQuery {

    public RoutinesQuerier(PolicyStore store) {
        super(store);
    }

    @Override
    public Collection<String> getAdminRoutineNames() throws PMException {
        return store.routines().getAdminRoutineNames();
    }

    @Override
    public Routine<?, ?> getAdminRoutine(String routineName) throws PMException {
        if (!store.routines().getAdminRoutineNames().contains(routineName)) {
            throw new RoutineDoesNotExistException(routineName);
        }

        return store.routines().getAdminRoutine(routineName);
    }
}
