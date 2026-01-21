package gov.nist.csd.pm.core.pap.operation;

import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.OperationExistsException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.query.OperationsQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A registry for storing Operations in memory.
 */
public class PluginRegistry {

    private final Map<String, Operation<?>> operations;

    public PluginRegistry() {
        operations = new HashMap<>();
    }

    /**
     * Get all operations as a map.
     * @return A Map of all operations indexed by name.
     */
    public Map<String, Operation<?>> getOperations() {
        return operations;
    }

    /**
     * Get all operations as a list.
     * @return A List of all the Operation objects.
     */
    public List<Operation<?>> getOperationsList() {
        return new ArrayList<>(operations.values());
    }

    /**
     * Get the Operation with the given name.
     * @param name The name of the operation.
     * @return The Operation object.
     * @throws OperationDoesNotExistException If the operation does not exist.
     */
    public Operation<?> getOperation(String name) throws OperationDoesNotExistException {
        Operation<?> operation = operations.get(name);
        if (operation == null) {
            throw new OperationDoesNotExistException(name);
        }

        return operation;
    }

    /**
     * Add an operation to the registry. The name cannot conflict with an existing operation in the policy
     * or the registry.
     * @param opQuery An OperationsQuery implementation to check for name conflict with operations stored in the policy.
     * @param operation The operation to add.
     * @throws PMException If there is an error checking if the operation exists or if the name does conflict
     */
    public void addOperation(OperationsQuery opQuery, Operation<?> operation) throws PMException {
        String name = operation.getName();
        boolean exists = opQuery.operationExists(name) || pluginExists(name);
        if (exists) {
            throw new OperationExistsException(name);
        }

        operations.put(name, operation);
    }

    /**
     * Remove the operation with the given name from the registry.
     * @param name The name of the operation to remove.
     */
    public void removeOperation(String name) {
        operations.remove(name);
    }

    /**
     * Returns true if a plugin operation with the given name exists.
     * @param pluginName The name of the plugin.
     * @return True if the plugin exists, false if not.
     */
    public boolean pluginExists(String pluginName) {
        return operations.containsKey(pluginName);
    }
}
