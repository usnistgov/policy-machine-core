package gov.nist.csd.pm.core.pap;

import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.OperationExistsException;
import gov.nist.csd.pm.core.pap.operation.AdminOperations;
import gov.nist.csd.pm.core.pap.operation.Operation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * In-process registry of live native (non-PML) {@link Operation} instances, replacing {@code PluginRegistry}.
 * A {@code Store} never persists the object itself, only a reference to its name — registering an operation
 * here is what makes that name resolvable to a live instance on this process. Registering is step one of a
 * two-step lifecycle: {@link #register(Operation)} only makes the implementation available, it does not persist
 * anything or make the operation exist in the policy; {@code OperationsModification.createOperation} is what
 * persists the reference and activates it.
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
     * For internal library use only, not part of the public embedding-application API surface. Checks that a
     * live implementation is registered for the given name without returning it — used at both write-path
     * (two-step {@code createOperation} lifecycle enforcement) and construction-time (fail-fast validation)
     * call sites that only care whether the name resolves, not what it resolves to.
     * @param name The operation name.
     * @throws OperationDoesNotExistException If no operation with this name is registered.
     */
    public void requireRegistered(String name) throws OperationDoesNotExistException {
        get(name);
    }
}
