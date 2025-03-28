package gov.nist.csd.pm.pap.pml.scope;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.AdminFunction;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.function.basic.builtin.PMLBuiltinFunctions;
import gov.nist.csd.pm.pap.pml.function.operation.PMLOperationWrapper;
import gov.nist.csd.pm.pap.pml.function.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.function.routine.PMLRoutineWrapper;
import gov.nist.csd.pm.pap.pml.function.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.PM_ADMIN_OBJECT;
import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.PM_ADMIN_PC;

public class ExecuteScope extends Scope<Value, AdminFunction<?>> {

    public ExecuteScope(PAP pap) throws PMException {
        // add constants
        Map<String, Value> constants = new HashMap<>(pap.getPMLConstants());
        constants.put(PM_ADMIN_PC.constantName(), new StringValue(PM_ADMIN_PC.nodeName()));
        constants.put(PM_ADMIN_OBJECT.constantName(), new StringValue(PM_ADMIN_OBJECT.nodeName()));
        setConstants(constants);

        // add pml operations and routines stored in PAP
        Map<String, AdminFunction<?>> functions = new HashMap<>();
        functions.putAll(new HashMap<>(PMLBuiltinFunctions.builtinFunctions()));
        functions.putAll(pap.getPMLOperations());
        functions.putAll(pap.getPMLRoutines());
        setFunctions(functions);

        // add custom operations from the PAP, could be PML or not PML based
        Collection<String> opNames = pap.query().operations().getAdminOperationNames();
        for (String opName : opNames) {
            Operation<?> operation = pap.query().operations().getAdminOperation(opName);
            if (operation instanceof PMLStmtsOperation pmlStmtsOperation) {
                addFunction(opName, pmlStmtsOperation);
            } else {
                addFunction(opName, new PMLOperationWrapper(operation));
            }
        }

        // same for routines
        Collection<String> routineNames = pap.query().routines().getAdminRoutineNames();
        for (String routineName : routineNames) {
            Routine<?> routine = pap.query().routines().getAdminRoutine(routineName);
            if (routine instanceof PMLStmtsRoutine pmlStmtsRoutine) {
                addFunction(routineName, pmlStmtsRoutine);
            } else {
                addFunction(routineName, new PMLRoutineWrapper(routine));
            }
        }
    }
}
