package gov.nist.csd.pm.core.pap.modification;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.RoutineExistsException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.exception.AdminAccessRightExistsException;
import gov.nist.csd.pm.core.common.exception.OperationExistsException;
import gov.nist.csd.pm.core.pap.admin.AdminOperations;
import gov.nist.csd.pm.core.pap.function.PluginRegistry;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.isAdminAccessRight;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.isWildcardAccessRight;

public class OperationsModifier extends Modifier implements OperationsModification {

    private PluginRegistry pluginRegistry;

    public OperationsModifier(PolicyStore store, PluginRegistry pluginRegistry) {
        super(store);
        this.pluginRegistry = pluginRegistry;
    }

    @Override
    public void setResourceOperations(AccessRightSet resourceOperations) throws PMException {
        checkSetResourceAccessRightsInput(resourceOperations);

        policyStore.operations().setResourceOperations(resourceOperations);
    }

    @Override
    public void createAdminOperation(Operation<?, ?> operation) throws PMException {
        if (AdminOperations.isAdminOperation(operation.getName())
            || policyStore.operations().getAdminOperationNames().contains(operation.getName())
            || pluginRegistry.getOperationNames().contains(operation.getName())) {
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
     *
     * @param accessRightSet The access right set to check.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected void checkSetResourceAccessRightsInput(AccessRightSet accessRightSet) throws PMException {
        for (String ar : accessRightSet) {
            if (isAdminAccessRight(ar) || isWildcardAccessRight(ar) ) {
                throw new AdminAccessRightExistsException(ar);
            }
        }
    }
}
