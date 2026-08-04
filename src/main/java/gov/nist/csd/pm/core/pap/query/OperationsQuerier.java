package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.NativeOperationRegistry;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.Collection;

public class OperationsQuerier extends Querier implements OperationsQuery {

    private final NativeOperationRegistry nativeOperationRegistry;

    public OperationsQuerier(PolicyStore store, NativeOperationRegistry nativeOperationRegistry) {
        super(store);
        this.nativeOperationRegistry = nativeOperationRegistry;
    }

    @Override
    public AccessRightSet getResourceAccessRights() throws PMException {
        return store.operations().getResourceAccessRights();
    }

    @Override
    public Collection<Operation<?>> getOperations() throws PMException {
        return new ArrayList<>(store.operations().getOperations());
    }

    @Override
    public Collection<String> getOperationNames() throws PMException {
        return new ArrayList<>(store.operations().getOperationNames());
    }

    @Override
    public Operation<?> getOperation(String name) throws PMException {
        if (nativeOperationRegistry.isProtected(name)) {
            return nativeOperationRegistry.get(name);
        } else if (!store.operations().operationExists(name)) {
            throw new OperationDoesNotExistException(name);
        }

        return store.operations().getOperation(name);
    }

    @Override
    public boolean operationExists(String operationName) throws PMException {
        return nativeOperationRegistry.isProtected(operationName) || store.operations().operationExists(operationName);
    }
}
