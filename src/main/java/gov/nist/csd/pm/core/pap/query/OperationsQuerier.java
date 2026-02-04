package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.PluginRegistry;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OperationsQuerier extends Querier implements OperationsQuery {

    private final PluginRegistry pluginRegistry;

    public OperationsQuerier(PolicyStore store, PluginRegistry pluginRegistry) {
        super(store);
        this.pluginRegistry = pluginRegistry;
    }

    @Override
    public AccessRightSet getResourceAccessRights() throws PMException {
        return store.operations().getResourceAccessRights();
    }

    @Override
    public Collection<Operation<?>> getOperations() throws PMException {
        List<Operation<?>> operations = new ArrayList<>(store.operations().getOperations());
        operations.addAll(pluginRegistry.getOperationsList());
        return operations;
    }

    @Override
    public Collection<String> getOperationNames() throws PMException {
        List<String> operationNames = new ArrayList<>(store.operations().getOperationNames());
        operationNames.addAll(pluginRegistry.getOperations().keySet());
        return operationNames;
    }

    @Override
    public Operation<?> getOperation(String name) throws PMException {
        if (pluginRegistry.pluginExists(name)) {
            return pluginRegistry.getOperation(name);
        }

        if (!store.operations().operationExists(name)) {
            throw new OperationDoesNotExistException(name);
        }

        return store.operations().getOperation(name);
    }

    @Override
    public boolean operationExists(String operationName) throws PMException {
        return pluginRegistry.pluginExists(operationName) || store.operations().operationExists(operationName);
    }
}
