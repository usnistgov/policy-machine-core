package gov.nist.csd.pm.pap.pml.scope;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.executable.AdminExecutable;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLBuiltinOperations;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperationWrapper;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutine;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutineWrapper;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.common.routine.Routine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.*;

public class ExecuteGlobalScope extends GlobalScope<Value, AdminExecutable<?>> {

    private ExecuteGlobalScope() {
        // buitin variables
        Map<String, Value> builtinConstants = new HashMap<>();

        builtinConstants.put(PM_ADMIN_PC.constantName(), new StringValue(PM_ADMIN_PC.nodeName()));
        builtinConstants.put(PM_ADMIN_OBJECT.constantName(), new StringValue(PM_ADMIN_OBJECT.nodeName()));

        addConstants(builtinConstants);

        // add builtin operations
        Map<String, PMLOperation> funcs = PMLBuiltinOperations.builtinFunctions();
        for (Map.Entry<String, PMLOperation> entry : funcs.entrySet()) {
            addExecutable(entry.getKey(), entry.getValue());
        }
    }

    public ExecuteGlobalScope(PAP pap) throws PMException {
        this();

        Map<String, Value> pmlConstants = pap.getPMLConstants();
        addConstants(pmlConstants);

        Map<String, PMLOperation> operations = pap.getPMLOperations();
        addExecutables(new HashMap<>(operations));

        Map<String, PMLRoutine> routines = pap.getPMLRoutines();
        addExecutables(new HashMap<>(routines));

        Collection<String> opNames = pap.query().operations().getAdminOperationNames();
        for (String opName : opNames) {
            Operation<?> operation = pap.query().operations().getAdminOperation(opName);
            if (operation instanceof PMLStmtsOperation pmlStmtsOperation) {
                addExecutable(opName, pmlStmtsOperation);
            } else {
                addExecutable(opName, new PMLOperationWrapper(operation));
            }
        }

        Collection<String> routineNames = pap.query().routines().getAdminRoutineNames();
        for (String routineName : routineNames) {
            Routine<?> routine = pap.query().routines().getAdminRoutine(routineName);
            if (routine instanceof PMLStmtsRoutine pmlStmtsRoutine) {
                addExecutable(routineName, pmlStmtsRoutine);
            } else {
                addExecutable(routineName, new PMLRoutineWrapper(routine));
            }
        }
    }
}
