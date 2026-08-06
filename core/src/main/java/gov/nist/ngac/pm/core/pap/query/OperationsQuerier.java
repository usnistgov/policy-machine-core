package gov.nist.ngac.pm.core.pap.query;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.JavaOperationRegistry;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.OperationKind;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.StatementVisitor;
import gov.nist.ngac.pm.core.pap.pml.statement.OperationDefinitionStatement;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.Collection;

/**
 * {@link OperationsQuery} implementation backed by the
 * {@link gov.nist.ngac.pm.core.pap.store.PolicyStore}'s operations store.
 */
public class OperationsQuerier extends Querier implements OperationsQuery {

    private final JavaOperationRegistry javaOperationRegistry;

    public OperationsQuerier(PolicyStore store, JavaOperationRegistry javaOperationRegistry) {
        super(store);
        this.javaOperationRegistry = javaOperationRegistry;
    }

    @Override
    public AccessRightSet getResourceAccessRights() throws PMException {
        return store.operations().getResourceAccessRights();
    }

    /**
     * Resolves every persisted row plus the always-present protected built-ins. JAVA-kind rows are always
     * resolved through the {@link JavaOperationRegistry}, never via a store shortcut.
     */
    @Override
    public Collection<Operation<?>> getOperations() throws PMException {
        Collection<Operation<?>> operations = new ArrayList<>(javaOperationRegistry.getProtectedOperations());

        for (String name : store.operations().getOperationNames()) {
            operations.add(resolveStoreOperation(name));
        }

        return operations;
    }

    @Override
    public Collection<String> getOperationNames() throws PMException {
        Collection<String> names = new ArrayList<>(javaOperationRegistry.getProtectedNames());
        names.addAll(store.operations().getOperationNames());
        return names;
    }

    @Override
    public Operation<?> getOperation(String name) throws PMException {
        if (javaOperationRegistry.isProtected(name)) {
            return javaOperationRegistry.get(name);
        }

        return resolveStoreOperation(name);
    }

    @Override
    public OperationKind getOperationKind(String name) throws PMException {
        if (javaOperationRegistry.isProtected(name)) {
            return OperationKind.JAVA;
        }

        return store.operations().getOperationKind(name);
    }

    @Override
    public boolean operationExists(String operationName) throws PMException {
        return javaOperationRegistry.isProtected(operationName) || store.operations().operationExists(operationName);
    }

    /**
     * Resolve a persisted operation by name: JAVA-kind through the registry, PML-kind by
     * recompiling the store's persisted PML text.
     */
    private Operation<?> resolveStoreOperation(String name) throws PMException {
        if (store.operations().getOperationKind(name) == OperationKind.JAVA) {
            return javaOperationRegistry.get(name);
        }

        String pmlText = store.operations().getOperationPml(name).orElseThrow(() -> new IllegalStateException(
            "operation '" + name + "' expected to be PML but no text was found"));
        return StatementVisitor.fromString(this, pmlText, OperationDefinitionStatement.class, OperationDefinitionStatement::getOperation);
    }
}
