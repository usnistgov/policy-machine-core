package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.exception.RoutineDoesNotExistException;
import gov.nist.csd.pm.pap.routine.Routine;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.*;

public class RoutinesQuerier extends Querier implements RoutinesQuery {

    public RoutinesQuerier(PolicyStore store) {
        super(store);
    }

    public RoutinesQuerier(Querier querier) {
        super(querier);
    }

    @Override
    public Collection<String> getAdminRoutineNames() throws PMException {
        return store.routines().getAdminRoutineNames();
    }

    @Override
    public Routine<?> getAdminRoutine(String routineName) throws PMException {
        if (!store.routines().getAdminRoutineNames().contains(routineName)) {
            throw new RoutineDoesNotExistException(routineName);
        }

        return store.routines().getAdminRoutine(routineName);
    }
}
