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

package gov.nist.ngac.pm.core.pap.operation;

import gov.nist.ngac.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.OperationExistsException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * In-process registry of live Java (non-PML) {@link Operation} instances. A store persists only a
 * reference to an operation's name. {@link #register(Operation)} is what makes that name resolvable to a
 * live instance on this process.
 */
public class JavaOperationRegistry {

    private final Map<String, Operation<?>> operations;
    private final Set<String> protectedNames;

    public JavaOperationRegistry() {
        this.operations = new HashMap<>();
        this.protectedNames = new HashSet<>();

        for (Operation<?> op : AdminOperations.ADMIN_OPERATIONS) {
            operations.put(op.getName(), op);
            protectedNames.add(op.getName());
        }
    }

    /**
     * Register a live Java operation implementation. The name cannot conflict with an already-registered
     * name (protected built-in or previously registered). There is no way to mark a caller-registered operation
     * as protected.
     * @param operation The operation to register.
     * @throws OperationExistsException If an operation with this name is already registered.
     */
    public void register(Operation<?> operation) throws OperationExistsException {
        String name = operation.getName();
        if (operations.containsKey(name)) {
            throw new OperationExistsException(name);
        }

        operations.put(name, operation);
    }

    /**
     * For internal library use only, not part of the public embedding-application API surface.
     * @param name The operation name.
     * @return True if the name is a protected built-in that cannot be deleted.
     */
    public boolean isProtected(String name) {
        return protectedNames.contains(name);
    }

    /**
     * For internal library use only, not part of the public embedding-application API surface.
     * @param name The operation name.
     * @return The registered live operation instance.
     * @throws OperationDoesNotExistException If no operation with this name is registered.
     */
    public Operation<?> get(String name) throws OperationDoesNotExistException {
        Operation<?> operation = operations.get(name);
        if (operation == null) {
            throw new OperationDoesNotExistException(name);
        }

        return operation;
    }

    /**
     * For internal library use only, not part of the public embedding-application API surface. Used to append
     * the always-present protected built-ins to a bulk operation listing — they have no store row of their own.
     * @return The protected built-in operations, unconditionally seeded on every registry.
     */
    public Collection<Operation<?>> getProtectedOperations() {
        List<Operation<?>> protectedOperations = new ArrayList<>();
        for (String name : protectedNames) {
            protectedOperations.add(operations.get(name));
        }
        return protectedOperations;
    }

    /**
     * For internal library use only, not part of the public embedding-application API surface.
     * @return The names of the protected built-in operations, unconditionally seeded on every registry.
     */
    public Set<String> getProtectedNames() {
        return new HashSet<>(protectedNames);
    }
}
