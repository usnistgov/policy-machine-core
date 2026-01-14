package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.ResourceOperation;
import gov.nist.csd.pm.core.pap.store.OperationsStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemoryOperationsStore extends MemoryStore implements OperationsStore {

    public MemoryOperationsStore(MemoryPolicy policy, MemoryTx tx, TxCmdTracker txCmdTracker) {
        super(policy, tx, txCmdTracker);
    }

    @Override
    public void setResourceAccessRights(AccessRightSet resourceAccessRights) throws PMException {
        AccessRightSet old = new AccessRightSet(policy.resourceAccessRights);

        policy.resourceAccessRights = resourceAccessRights;

        txCmdTracker.trackOp(tx, new TxCmd.SetResourceOperationsTxCmd(
                old,
            resourceAccessRights)
        );
    }

    @Override
    public void createResourceOperation(ResourceOperation operation) throws PMException {
        policy.resourceOps.put(operation.getName(), operation);
    }

    @Override
    public void deleteResourceOperation(String operation) throws PMException {
        policy.resourceOps.remove(operation);
    }

    @Override
    public void createAdminOperation(AdminOperation<?> operation) throws PMException {
        policy.adminOps.put(operation.getName(), operation);
    }

    @Override
    public void deleteAdminOperation(String operation) throws PMException {
        policy.adminOps.remove(operation);
    }

    @Override
    public AccessRightSet getResourceAccessRights() throws PMException {
        return policy.resourceAccessRights;
    }

    @Override
    public Collection<String> getResourceOperationNames() throws PMException {
        return new ArrayList<>(policy.resourceOps.keySet());
    }

    @Override
    public ResourceOperation getResourceOperation(String operationName) throws PMException {
        return policy.resourceOps.get(operationName);
    }

    @Override
    public Collection<String> getAdminOperationNames() throws PMException {
        return new ArrayList<>(policy.adminOps.keySet());
    }

    @Override
    public AdminOperation<?> getAdminOperation(String operationName) throws PMException {
        return policy.adminOps.get(operationName);
    }
}
