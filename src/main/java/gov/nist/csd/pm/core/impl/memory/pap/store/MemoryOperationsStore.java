package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.ResourceOperation;
import gov.nist.csd.pm.core.pap.function.Routine;
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

        txCmdTracker.trackOp(tx, new TxCmd.SetResourceOperationsTxCmd(
                old,
            resourceAccessRights)
        );
    }

    @Override
    public void createResourceOperation(ResourceOperation<?> operation) throws PMException {
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

    @Override
    public void createAdminRoutine(Routine<?> routine) throws PMException {
        policy.routines.put(routine.getName(), routine);

        txCmdTracker.trackOp(tx, new TxCmd.CreateAdminRoutine(routine));
    }

    @Override
    public void deleteAdminRoutine(String name) throws PMException {
        Routine<?> routine = policy.routines.get(name);

        policy.routines.remove(name);

        txCmdTracker.trackOp(tx, new TxCmd.DeleteAdminRoutine(routine));
    }

    @Override
    public Collection<String> getAdminRoutineNames() throws PMException {
        return new ArrayList<>(policy.routines.keySet());
    }

    @Override
    public Routine<?> getAdminRoutine(String routineName) throws PMException {
        return policy.routines.get(routineName);
    }

    @Override
    public boolean operationExists(String operationName) throws PMException {
        return policy.resourceOps.containsKey(operationName) ||
            policy.adminOps.containsKey(operationName) ||
            policy.routines.containsKey(operationName);
    }
}
