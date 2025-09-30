package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.PluginRegistry;
import gov.nist.csd.pm.core.pap.function.op.Operation;
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
    public AccessRightSet getResourceOperations() throws PMException {
        return store.operations().getResourceOperations();
    }

    @Override
    public Collection<String> getAdminOperationNames() throws PMException {
        Set<String> adminOperationNames = new HashSet<>(store.operations().getAdminOperationNames());
        adminOperationNames.addAll(pluginRegistry.getOperationNames());
        return adminOperationNames;
    }

    @Override
    public Operation<?, ?> getAdminOperation(String operationName) throws PMException {
        if (pluginRegistry.getOperationNames().contains(operationName)) {
            return pluginRegistry.getOperation(operationName);
        }

        if (!store.operations().getAdminOperationNames().contains(operationName)) {
            throw new OperationDoesNotExistException(operationName);
        }

        return store.operations().getAdminOperation(operationName);
    }
}
