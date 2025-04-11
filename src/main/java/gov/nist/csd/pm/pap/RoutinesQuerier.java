package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.exception.RoutineDoesNotExistException;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.pap.query.RoutinesQuery;
import gov.nist.csd.pm.pap.store.PolicyStore;

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
