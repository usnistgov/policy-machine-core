package gov.nist.ngac.pm.core.impl.memory.pap.store;

import gov.nist.ngac.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.store.TxCmd.DeleteOperation;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.OperationKind;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperation;
import gov.nist.ngac.pm.core.pap.store.OperationsStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * {@link OperationsStore} implementation backed by the in-memory {@link MemoryPolicy} operation map.
 */
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
    public Collection<String> getOperationNames() throws PMException {
        return new ArrayList<>(policy.operations.keySet());
    }

    @Override
    public Optional<String> getOperationPml(String name) throws PMException {
        Operation<?> operation = policy.operations.get(name);
        if (!(operation instanceof PMLOperation)) {
            return Optional.empty();
        }

        return Optional.of(operation.toString());
    }

    @Override
    public OperationKind getOperationKind(String name) throws PMException {
        Operation<?> operation = policy.operations.get(name);
        if (operation == null) {
            throw new OperationDoesNotExistException(name);
        }
        return operation instanceof PMLOperation ? OperationKind.PML : OperationKind.JAVA;
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
