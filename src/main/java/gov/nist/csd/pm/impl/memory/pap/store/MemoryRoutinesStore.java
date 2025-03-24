package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.executable.routine.Routine;
import gov.nist.csd.pm.pap.store.RoutinesStore;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryRoutinesStore extends MemoryStore implements RoutinesStore {
    public MemoryRoutinesStore(MemoryPolicy policy, MemoryTx tx, TxCmdTracker txCmdTracker) {
        super(policy, tx, txCmdTracker);
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
    public Routine getAdminRoutine(String routineName) throws PMException {
        return policy.routines.get(routineName);
    }
}