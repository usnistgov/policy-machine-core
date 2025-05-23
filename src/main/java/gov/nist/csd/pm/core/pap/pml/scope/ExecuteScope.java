package gov.nist.csd.pm.core.pap.pml.scope;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.AdminFunction;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.function.basic.builtin.PMLBuiltinFunctions;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLStmtsOperation;
import gov.nist.csd.pm.core.pap.pml.function.routine.PMLStmtsRoutine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_OBJECT;
import static gov.nist.csd.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_PC;

public class ExecuteScope extends Scope<Object, AdminFunction<?, ?>> {

    public ExecuteScope(PAP pap) throws PMException {
        // add constants
        Map<String, Object> constants = new HashMap<>();
        constants.put(PM_ADMIN_PC.constantName(), PM_ADMIN_PC.nodeName());
        constants.put(PM_ADMIN_OBJECT.constantName(), PM_ADMIN_OBJECT.nodeName());
        setConstants(constants);

        // add pml operations and routines stored in PAP
        Map<String, AdminFunction<?, ?>> functions = new HashMap<>();
        functions.putAll(new HashMap<>(PMLBuiltinFunctions.builtinFunctions()));
        setFunctions(functions);

        // add custom operations from the PAP, could be PML or not PML based
        Collection<String> opNames = pap.query().operations().getAdminOperationNames();
        for (String opName : opNames) {
            Operation<?, ?> operation = pap.query().operations().getAdminOperation(opName);
            addFunction(opName, operation);
        }

        // same for routines
        Collection<String> routineNames = pap.query().routines().getAdminRoutineNames();
        for (String routineName : routineNames) {
            Routine<?, ?> routine = pap.query().routines().getAdminRoutine(routineName);
            addFunction(routineName, routine);
        }
    }
}
