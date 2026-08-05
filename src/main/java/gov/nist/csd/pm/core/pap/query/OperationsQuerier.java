package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.NativeOperationRegistry;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.OperationKind;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.StatementVisitor;
import gov.nist.csd.pm.core.pap.pml.statement.OperationDefinitionStatement;
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

    /**
     * Resolves every persisted row plus the always-present protected built-ins. NATIVE-kind rows are always
     * resolved through the {@link NativeOperationRegistry}, never via a store shortcut.
     */
    @Override
    public Collection<Operation<?>> getOperations() throws PMException {
        Collection<Operation<?>> operations = new ArrayList<>(nativeOperationRegistry.getProtectedOperations());

        for (String name : store.operations().getOperationNames()) {
            operations.add(resolveStoreOperation(name));
        }

        return operations;
    }

    @Override
    public Collection<String> getOperationNames() throws PMException {
        Collection<String> names = new ArrayList<>(nativeOperationRegistry.getProtectedNames());
        names.addAll(store.operations().getOperationNames());
        return names;
    }

    @Override
    public Operation<?> getOperation(String name) throws PMException {
        if (nativeOperationRegistry.isProtected(name)) {
            return nativeOperationRegistry.get(name);
        }

        return resolveStoreOperation(name);
    }

    @Override
    public OperationKind getOperationKind(String name) throws PMException {
        if (nativeOperationRegistry.isProtected(name)) {
            return OperationKind.NATIVE;
        }

        return store.operations().getOperationKind(name);
    }

    @Override
    public boolean operationExists(String operationName) throws PMException {
        return nativeOperationRegistry.isProtected(operationName) || store.operations().operationExists(operationName);
    }

    /**
     * Resolve a persisted operation by name: NATIVE-kind through the registry, PML-kind by
     * recompiling the store's persisted PML text.
     */
    private Operation<?> resolveStoreOperation(String name) throws PMException {
        if (store.operations().getOperationKind(name) == OperationKind.NATIVE) {
            return nativeOperationRegistry.get(name);
        }

        String pmlText = store.operations().getOperationPml(name).orElseThrow(() -> new IllegalStateException(
            "operation '" + name + "' expected to be PML but no text was found"));
        return StatementVisitor.fromString(this, pmlText, OperationDefinitionStatement.class, OperationDefinitionStatement::getOperation);
    }
}
