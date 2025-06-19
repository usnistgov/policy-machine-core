package gov.nist.csd.pm.core.pap.pml.scope;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.AdminFunction;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.function.basic.builtin.PMLBuiltinFunctions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ExecuteScope extends Scope<Object, AdminFunction<?, ?>> {

    public ExecuteScope(PAP pap) throws PMException {
        // add constants
        Map<String, Object> constants = new HashMap<>();
        for (AdminPolicyNode adminPolicyNode : AdminPolicyNode.values()) {
            constants.put(adminPolicyNode.constantName(), adminPolicyNode.nodeName());
        }
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
