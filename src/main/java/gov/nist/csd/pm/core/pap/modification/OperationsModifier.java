package gov.nist.csd.pm.core.pap.modification;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.exception.AdminAccessRightExistsException;
import gov.nist.csd.pm.core.common.exception.OperationExistsException;
import gov.nist.csd.pm.core.pap.admin.AdminOperations;
import gov.nist.csd.pm.core.pap.function.PluginRegistry;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.ResourceOperation;
import gov.nist.csd.pm.core.pap.store.PolicyStore;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.isAdminAccessRight;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.isWildcardAccessRight;

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
    public void createResourceOperation(ResourceOperation operation) throws PMException {
        if (operationExists(operation.getName())) {
            throw new OperationExistsException(operation.getName());
        }

        policyStore.operations().createResourceOperation(operation);
    }

    @Override
    public void deleteResourceOperation(String operation) throws PMException {
        if (!policyStore.operations().getAdminOperationNames().contains(operation)) {
            return;
        }

        policyStore.operations().deleteResourceOperation(operation);
    }

    @Override
    public void createAdminOperation(AdminOperation<?> operation) throws PMException {
        if (operationExists(operation.getName())) {
            throw new OperationExistsException(operation.getName());
        }

        policyStore.operations().createAdminOperation(operation);
    }

    @Override
    public void deleteAdminOperation(String operation) throws PMException {
        if (pluginRegistry.getOperationNames().contains(operation)) {
            pluginRegistry.removeOperation(operation);
            return;
        }

        // return without error if the operation does not exist or is a built in admin op such as assign
        if (AdminOperations.ADMIN_OP_NAMES.contains(operation)
            || !policyStore.operations().getAdminOperationNames().contains(operation)) {
            return;
        }

        policyStore.operations().deleteAdminOperation(operation);
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
     * check if operation admin or resource exists with the name
     */
    private boolean operationExists(String name) throws PMException {
        return AdminOperations.isAdminOperation(name)
            || policyStore.operations().getAdminOperationNames().contains(name)
            || pluginRegistry.getOperationNames().contains(name)
            || policyStore.operations().getResourceOperationNames().contains(name);
    }
}
