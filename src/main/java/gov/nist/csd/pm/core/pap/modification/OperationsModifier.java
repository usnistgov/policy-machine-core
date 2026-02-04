package gov.nist.csd.pm.core.pap.modification;

import static gov.nist.csd.pm.core.pap.modification.GraphModifier.isAdminAccessRight;
import static gov.nist.csd.pm.core.pap.modification.GraphModifier.isWildcardAccessRight;

import gov.nist.csd.pm.core.common.exception.AdminAccessRightExistsException;
import gov.nist.csd.pm.core.common.exception.OperationExistsException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.AdminOperations;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.PluginRegistry;
import gov.nist.csd.pm.core.pap.store.PolicyStore;

public class OperationsModifier extends Modifier implements OperationsModification {

    private PluginRegistry pluginRegistry;

    public OperationsModifier(PolicyStore store, PluginRegistry pluginRegistry) {
        super(store);
        this.pluginRegistry = pluginRegistry;
    }

    @Override
    public void setResourceAccessRights(AccessRightSet resourceAccessRights) throws PMException {
        checkSetResourceAccessRightsInput(resourceAccessRights);

        policyStore.operations().setResourceAccessRights(resourceAccessRights);
    }

    @Override
    public void createOperation(Operation<?> operation) throws PMException {
        if (operationExists(operation.getName())) {
            throw new OperationExistsException(operation.getName());
        }

        policyStore.operations().createOperation(operation);
    }

    @Override
    public void deleteOperation(String name) throws PMException {
        if (pluginRegistry.pluginExists(name)) {
            throw new CannotDeletePluginOperationException(name);
        } else if (!policyStore.operations().operationExists(name)) {
            return;
        }

        policyStore.operations().deleteOperation(name);
    }

    /**
     * Check that the provided resource operations are not existing admin access rights, operations or routines.
     */
    private void checkSetResourceAccessRightsInput(AccessRightSet accessRightSet) throws PMException {
        for (String ar : accessRightSet) {
            if (isAdminAccessRight(ar) || isWildcardAccessRight(ar) ) {
                throw new AdminAccessRightExistsException(ar);
            }
        }
    }

    /*
     * check if operation exists with the name
     */
    private boolean operationExists(String name) throws PMException {
        return AdminOperations.isAdminOperation(name)
            || policyStore.operations().operationExists(name)
            || pluginRegistry.pluginExists(name);
    }

    static class CannotDeletePluginOperationException extends PMException {

        public CannotDeletePluginOperationException(String name) {
            super("cannot delete plugin operation " + name);
        }
    }
}
