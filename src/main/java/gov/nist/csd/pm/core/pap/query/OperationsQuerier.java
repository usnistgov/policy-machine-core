package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.RoutineDoesNotExistException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.BasicFunction;
import gov.nist.csd.pm.core.pap.function.PluginRegistry;
import gov.nist.csd.pm.core.pap.function.QueryOperation;
import gov.nist.csd.pm.core.pap.function.ResourceOperation;
import gov.nist.csd.pm.core.pap.function.Routine;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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

        if (!store.operations().operationExists(operationName)) {
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

        if (!store.operations().operationExists(operationName)) {
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

        if (!store.operations().operationExists(routineName)) {
            throw new RoutineDoesNotExistException(routineName);
        }

        return store.operations().getAdminRoutine(routineName);
    }

    @Override
    public Collection<String> getQueryOperationNames() throws PMException {
        Collection<String> queryOpNames = new HashSet<>(store.operations().getQueryOperationNames());
        queryOpNames.addAll(pluginRegistry.getQueryOperationNames());
        return queryOpNames;
    }

    @Override
    public QueryOperation<?> getQueryOperation(String name) throws PMException {
        if (pluginRegistry.getRoutineNames().contains(name)) {
            return pluginRegistry.getQueryOperation(name);
        }

        if (!store.operations().operationExists(name)) {
            throw new OperationDoesNotExistException(name);
        }

        return store.operations().getQueryOperation(name);
    }

    @Override
    public Collection<String> getBasicFunctionNames() throws PMException {
        Collection<String> basicFuncNames = new HashSet<>(store.operations().getBasicFunctionNames());
        basicFuncNames.addAll(pluginRegistry.getBasicFunctionNames());
        return basicFuncNames;
    }

    @Override
    public BasicFunction<?> getBasicFunction(String name) throws PMException {
        if (pluginRegistry.getBasicFunctionNames().contains(name)) {
            return pluginRegistry.getBasicFunction(name);
        }

        if (!store.operations().operationExists(name)) {
            throw new OperationDoesNotExistException(name);
        }

        return store.operations().getBasicFunction(name);
    }

    @Override
    public boolean operationExists(String operationName) throws PMException {
        return pluginRegistry.pluginExists(operationName) || store.operations().operationExists(operationName);
    }
}
