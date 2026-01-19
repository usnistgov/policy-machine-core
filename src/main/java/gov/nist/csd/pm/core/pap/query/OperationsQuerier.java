package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.RoutineDoesNotExistException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.PluginRegistry;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.ResourceOperation;
import gov.nist.csd.pm.core.pap.function.Routine;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class OperationsQuerier extends Querier implements OperationsQuery {

    private PluginRegistry pluginRegistry;

    public OperationsQuerier(PolicyStore store, PluginRegistry pluginRegistry) {
        super(store);
        this.pluginRegistry = pluginRegistry;
    }

    @Override
    public AccessRightSet getResourceAccessRights() throws PMException {
        return store.operations().getResourceAccessRights();
    }

    @Override
    public Collection<String> getResourceOperationNames() throws PMException {
        return store.operations().getResourceOperationNames();
    }

    @Override
    public ResourceOperation<?> getResourceOperation(String operationName) throws PMException {
        if (pluginRegistry.getResourceOperationNames().contains(operationName)) {
            return pluginRegistry.getResourceOperation(operationName);
        }

        if (!store.operations().getAdminOperationNames().contains(operationName)) {
            throw new OperationDoesNotExistException(operationName);
        }

        return store.operations().getResourceOperation(operationName);
    }

    @Override
    public Collection<String> getAdminOperationNames() throws PMException {
        Set<String> adminOperationNames = new HashSet<>(store.operations().getAdminOperationNames());
        adminOperationNames.addAll(pluginRegistry.getAdminOperationNames());
        return adminOperationNames;
    }

    @Override
    public AdminOperation<?> getAdminOperation(String operationName) throws PMException {
        if (pluginRegistry.getAdminOperationNames().contains(operationName)) {
            return pluginRegistry.getAdminOperation(operationName);
        }

        if (!store.operations().getAdminOperationNames().contains(operationName)) {
            throw new OperationDoesNotExistException(operationName);
        }

        return store.operations().getAdminOperation(operationName);
    }

    @Override
    public Collection<String> getAdminRoutineNames() throws PMException {
        Collection<String> adminRoutineNames = new HashSet<>(store.operations().getAdminRoutineNames());
        adminRoutineNames.addAll(pluginRegistry.getRoutineNames());
        return adminRoutineNames;
    }

    @Override
    public Routine<?> getAdminRoutine(String routineName) throws PMException {
        if (pluginRegistry.getRoutineNames().contains(routineName)) {
            return pluginRegistry.getRoutine(routineName);
        }

        if (!store.operations().getAdminRoutineNames().contains(routineName)) {
            throw new RoutineDoesNotExistException(routineName);
        }

        return store.operations().getAdminRoutine(routineName);
    }

    @Override
    public boolean operationExists(String operationName) throws PMException {
        return pluginRegistry.pluginExists(operationName) || store.operations().operationExists(operationName);
    }
}
