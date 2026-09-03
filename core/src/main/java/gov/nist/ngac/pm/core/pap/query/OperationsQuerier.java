/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

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
 * An {@link OperationsQuery} implementation backed by the policy store's operations store.
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
     * Resolves a persisted operation by name, using the registry for JAVA-kind operations and
     * recompiling the stored PML text for PML-kind ones.
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
