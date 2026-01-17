package gov.nist.csd.pm.core.pap.modification;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.RoutineExistsException;
import gov.nist.csd.pm.core.pap.function.PluginRegistry;
import gov.nist.csd.pm.core.pap.function.Routine;
import gov.nist.csd.pm.core.pap.store.PolicyStore;

public class RoutinesModifier extends Modifier implements RoutinesModification {

    private PluginRegistry pluginRegistry;

    public RoutinesModifier(PolicyStore store, PluginRegistry pluginRegistry) {
        super(store);
        this.pluginRegistry = pluginRegistry;
    }

    @Override
    public void createAdminRoutine(Routine<?> routine) throws PMException {
        String routineName = routine.getName();

        if (policyStore.routines().getAdminRoutineNames().contains(routineName)
            || pluginRegistry.getRoutineNames().contains(routineName)) {
            throw new RoutineExistsException(routineName);
        }

        policyStore.routines().createAdminRoutine(routine);
    }

    @Override
    public void deleteAdminRoutine(String name) throws PMException {
        if (pluginRegistry.getRoutineNames().contains(name)) {
            pluginRegistry.removeRoutine(name);
            return;
        }

        if (!policyStore.routines().getAdminRoutineNames().contains(name)) {
            return;
        }

        policyStore.routines().deleteAdminRoutine(name);
    }
}