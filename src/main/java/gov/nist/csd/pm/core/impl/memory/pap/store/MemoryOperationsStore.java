package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.impl.memory.pap.store.TxCmd.DeleteFunction;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.BasicFunction;
import gov.nist.csd.pm.core.pap.function.Function;
import gov.nist.csd.pm.core.pap.function.QueryOperation;
import gov.nist.csd.pm.core.pap.function.ResourceOperation;
import gov.nist.csd.pm.core.pap.function.Routine;
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
    public void createResourceOperation(ResourceOperation<?> operation) throws PMException {
        policy.resourceOps.put(operation.getName(), operation);
    }

    @Override
    public void createAdminOperation(AdminOperation<?> operation) throws PMException {
        policy.adminOps.put(operation.getName(), operation);
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
    public ResourceOperation<?> getResourceOperation(String operationName) throws PMException {
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
    public void createQueryOperation(QueryOperation<?> operation) throws PMException {
        policy.queryOps.put(operation.getName(), operation);

        txCmdTracker.trackOp(tx, new TxCmd.CreateQueryOperation(routine));
    }

    @Override
    public void createBasicFunction(BasicFunction<?> function) throws PMException {
        policy.routines.put(function.getName(), function);

        txCmdTracker.trackOp(tx, new TxCmd.CreateBasicFunction(function));
    }

    @Override
    public void deleteOperation(String name) throws PMException {
        Function<?> remove = policy.adminOps.remove(name);
        if (remove == null) {
            txCmdTracker.trackOp(tx, new DeleteFunction(remove));
            return;
        }
        remove = policy.resourceOps.remove(name);
        if (remove == null) {
            txCmdTracker.trackOp(tx, new DeleteFunction(remove));
            return;
        }
        remove = policy.routines.remove(name);
        if (remove == null) {
            txCmdTracker.trackOp(tx, new DeleteFunction(remove));
            return;
        }
        remove = policy.basicFuncs.remove(name);
        if (remove == null) {
            txCmdTracker.trackOp(tx, new DeleteFunction(remove));
            return;
        }
        remove = policy.queryOps.remove(name);
        if (remove == null) {
            txCmdTracker.trackOp(tx, new DeleteFunction(remove));
        }
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
    public Collection<String> getQueryOperationNames() throws PMException {
        return new ArrayList<>(policy.queryOps.keySet());
    }

    @Override
    public QueryOperation<?> getQueryOperation(String name) throws PMException {
        return policy.queryOps.get(name);
    }

    @Override
    public Collection<String> getBasicFunctionNames() throws PMException {
        return new ArrayList<>(policy.basicFuncs.keySet());
    }

    @Override
    public BasicFunction<?> getBasicFunction(String name) throws PMException {
        return policy.basicFuncs.get(name);
    }

    @Override
    public boolean operationExists(String operationName) throws PMException {
        return policy.resourceOps.containsKey(operationName) ||
            policy.adminOps.containsKey(operationName) ||
            policy.routines.containsKey(operationName);
    }
}
