package gov.nist.ngac.pm.core.pap;

import gov.nist.ngac.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.OperationExistsException;
import gov.nist.ngac.pm.core.pap.operation.AdminOperations;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * In-process registry of live native (non-PML) {@link Operation} instances. A store persists only a
 * reference to an operation's name. {@link #register(Operation)} is what makes that name resolvable to a
 * live instance on this process.
 */
public class NativeOperationRegistry {

    private final Map<String, Operation<?>> operations;
    private final Set<String> protectedNames;

    public NativeOperationRegistry() {
        this.operations = new HashMap<>();
        this.protectedNames = new HashSet<>();

        for (Operation<?> op : AdminOperations.ADMIN_OPERATIONS) {
            operations.put(op.getName(), op);
            protectedNames.add(op.getName());
        }
    }

    /**
     * Register a live native operation implementation. The name cannot conflict with an already-registered
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
