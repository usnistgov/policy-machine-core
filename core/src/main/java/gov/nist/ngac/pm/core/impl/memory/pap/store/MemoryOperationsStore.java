/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

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
 * An {@link OperationsStore} implementation backed by an in-memory map of operations.
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
