package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.RoutineDoesNotExistException;
import gov.nist.csd.pm.core.pap.function.PluginRegistry;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.store.PolicyStore;

import java.util.Collection;
import java.util.HashSet;

public class RoutinesQuerier extends Querier implements RoutinesQuery {

    private PluginRegistry pluginRegistry;

    public RoutinesQuerier(PolicyStore store, PluginRegistry pluginRegistry) {
        super(store);
        this.pluginRegistry = pluginRegistry;
    }

    @Override
    public Collection<String> getAdminRoutineNames() throws PMException {
        Collection<String> adminRoutineNames = new HashSet<>(store.routines().getAdminRoutineNames());
        adminRoutineNames.addAll(pluginRegistry.getRoutineNames());
        return adminRoutineNames;
    }

    @Override
    public Routine<?> getAdminRoutine(String routineName) throws PMException {
        if (pluginRegistry.getRoutineNames().contains(routineName)) {
            return pluginRegistry.getRoutine(routineName);
        }

        if (!store.routines().getAdminRoutineNames().contains(routineName)) {
            throw new RoutineDoesNotExistException(routineName);
        }

        return store.routines().getAdminRoutine(routineName);
    }
}
