package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.store.OperationsStore;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryOperationsStore extends MemoryStore implements OperationsStore {

    public MemoryOperationsStore(MemoryPolicy policy, MemoryTx tx, TxCmdTracker txCmdTracker) {
        super(policy, tx, txCmdTracker);
    }

    @Override
    public void setResourceOperations(AccessRightSet accessRightSet) throws PMException {
        AccessRightSet old = new AccessRightSet(policy.resourceOperations);

        policy.resourceOperations = accessRightSet;

        txCmdTracker.trackOp(tx, new TxCmd.SetResourceOperationsTxCmd(
                old,
                accessRightSet)
        );
    }

    @Override
    public void createAdminOperation(Operation<?, ?> operation) throws PMException {
        policy.operations.put(operation.getName(), operation);
    }

    @Override
    public void deleteAdminOperation(String operation) throws PMException {
        policy.operations.remove(operation);
    }

    @Override
    public AccessRightSet getResourceOperations() throws PMException {
        return policy.resourceOperations;
    }

    @Override
    public Collection<String> getAdminOperationNames() throws PMException {
        return new ArrayList<>(policy.operations.keySet());
    }

    @Override
    public Operation<?, ?> getAdminOperation(String operationName) throws PMException {
        return policy.operations.get(operationName);
    }
}
