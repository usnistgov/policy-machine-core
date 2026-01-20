package gov.nist.csd.pm.core.pap.modification;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.isAdminAccessRight;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.isWildcardAccessRight;

import gov.nist.csd.pm.core.common.exception.AdminAccessRightExistsException;
import gov.nist.csd.pm.core.common.exception.FunctionExistsException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.admin.AdminOperations;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.BasicFunction;
import gov.nist.csd.pm.core.pap.operation.PluginRegistry;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.ResourceOperation;
import gov.nist.csd.pm.core.pap.operation.Routine;
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
    public void createResourceOperation(ResourceOperation<?> operation) throws PMException {
        if (operationExists(operation.getName())) {
            throw new FunctionExistsException(operation.getName());
        }

        policyStore.operations().createResourceOperation(operation);
    }

    @Override
    public void createAdminOperation(AdminOperation<?> operation) throws PMException {
        if (operationExists(operation.getName())) {
            throw new FunctionExistsException(operation.getName());
        }

        policyStore.operations().createAdminOperation(operation);
    }

    @Override
    public void createAdminRoutine(Routine<?> routine) throws PMException {
        if (operationExists(routine.getName())) {
            throw new FunctionExistsException(routine.getName());
        }

        policyStore.operations().createAdminRoutine(routine);
    }

    @Override
    public void createQueryOperation(QueryOperation<?> operation) throws PMException {
        if (operationExists(operation.getName())) {
            throw new FunctionExistsException(operation.getName());
        }

        policyStore.operations().createQueryOperation(operation);
    }

    @Override
    public void createBasicFunction(BasicFunction<?> function) throws PMException {
        if (operationExists(function.getName())) {
            throw new FunctionExistsException(function.getName());
        }

        policyStore.operations().createBasicFunction(function);
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
     * check if operation admin or resource exists with the name
     */
    private boolean operationExists(String name) throws PMException {
        return AdminOperations.isAdminOperation(name)
            || policyStore.operations().getAdminOperationNames().contains(name)
            || pluginRegistry.pluginExists(name)
            || policyStore.operations().getResourceOperationNames().contains(name)
            || policyStore.operations().getAdminRoutineNames().contains(name);
    }

    static class CannotDeletePluginOperationException extends PMException {

        public CannotDeletePluginOperationException(String name) {
            super("cannot delete plugin operation " + name);
        }
    }
}
