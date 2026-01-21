package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.impl.memory.pap.store.TxCmd.DeleteOperation;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.store.OperationsStore;
import java.util.ArrayList;
import java.util.Collection;

public class MemoryOperationsStore extends MemoryStore implements OperationsStore {

    public MemoryOperationsStore(MemoryPolicy policy, MemoryTx tx, TxCmdTracker txCmdTracker) {
        super(policy, tx, txCmdTracker);
    }

    @Override
    public void setResourceAccessRights(AccessRightSet resourceAccessRights) throws PMException {
        AccessRightSet old = new AccessRightSet(policy.resourceAccessRights);

        policy.resourceAccessRights = resourceAccessRights;

        txCmdTracker.trackOp(tx, new TxCmd.SetResourceOperationsTxCmd(old, resourceAccessRights));
    }

    @Override
    public void createOperation(Operation<?> operation) throws PMException {
        policy.operations.put(operation.getName(), operation);
        txCmdTracker.trackOp(tx, new TxCmd.CreateOperationTxCmd(operation));
    }

    @Override
    public AccessRightSet getResourceAccessRights() throws PMException {
        return policy.resourceAccessRights;
    }

    @Override
    public Collection<Operation<?>> getOperations() throws PMException {
        return new ArrayList<>(policy.operations.values());
    }

    @Override
    public Collection<String> getOperationNames() throws PMException {
        return new ArrayList<>(policy.operations.keySet());
    }

    @Override
    public Operation<?> getOperation(String name) throws PMException {
        return policy.operations.get(name);
    }

    @Override
    public void deleteOperation(String name) throws PMException {
        Operation<?> remove = policy.operations.remove(name);
        if (remove == null) {
            return;
        }

        txCmdTracker.trackOp(tx, new DeleteOperation(remove));
    }

    @Override
    public boolean operationExists(String operationName) throws PMException {
        return policy.operations.containsKey(operationName);
    }
}
