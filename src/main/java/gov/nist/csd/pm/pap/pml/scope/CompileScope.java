package gov.nist.csd.pm.pap.pml.scope;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.routine.Routine;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperationWrapper;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutine;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutineWrapper;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.PM_ADMIN_OBJECT;
import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.PM_ADMIN_PC;
import static gov.nist.csd.pm.pap.pml.executable.operation.PMLBuiltinOperations.builtinFunctions;

public class CompileScope extends Scope<Variable, PMLExecutableSignature> {

    public CompileScope() {
        Map<String, Variable> constants = new HashMap<>();

        // admin policy nodes constants
        constants.put(PM_ADMIN_PC.constantName(), new Variable(PM_ADMIN_PC.constantName(), Type.string(), true));
        constants.put(PM_ADMIN_OBJECT.constantName(), new Variable(PM_ADMIN_OBJECT.constantName(), Type.string(), true));
        setConstants(constants);

        // add builtin operations
        Map<String, PMLExecutableSignature> executables = new HashMap<>();
        Map<String, PMLOperation> funcs = builtinFunctions();
        for (Map.Entry<String, PMLOperation> func : funcs.entrySet()) {
            executables.put(func.getKey(), func.getValue().getSignature());
        }
        setExecutables(executables);
    }

    public CompileScope(PAP pap) throws PMException {
        // add constants
        Map<String, Variable> constants = new HashMap<>();
        constants.put(PM_ADMIN_PC.constantName(), new Variable(PM_ADMIN_PC.nodeName(), Type.string(), true));
        constants.put(PM_ADMIN_OBJECT.constantName(), new Variable(PM_ADMIN_OBJECT.nodeName(), Type.string(), true));

        Map<String, Value> pmlConstants = pap.getPMLConstants();
        for (Map.Entry<String, Value> pmlConstant : pmlConstants.entrySet()) {
            String key = pmlConstant.getKey();
            Value value = pmlConstant.getValue();
            constants.put(key, new Variable(key, value.getType(), true));
        }
        setConstants(constants);

        // add pml operations and routines stored in PAP
        Map<String, PMLExecutableSignature> executables = new HashMap<>();
        for (Map.Entry<String, PMLOperation> e : builtinFunctions().entrySet()) {
            executables.put(e.getKey(), e.getValue().getSignature());
        }
        for (Map.Entry<String, PMLOperation> pmlOp : pap.getPMLOperations().entrySet()) {
            String key = pmlOp.getKey();
            PMLOperation value = pmlOp.getValue();
            executables.put(key, value.getSignature());
        }
        for (Map.Entry<String, PMLRoutine> pmlRoutine : pap.getPMLRoutines().entrySet()) {
            String key = pmlRoutine.getKey();
            PMLRoutine value = pmlRoutine.getValue();
            executables.put(key, value.getSignature());
        }
        setExecutables(executables);

        // add custom operations from the PAP, could be PML or not PML based
        Collection<String> opNames = pap.query().operations().getAdminOperationNames();
        for (String opName : opNames) {
            Operation<?> operation = pap.query().operations().getAdminOperation(opName);
            if (operation instanceof PMLStmtsOperation pmlStmtsOperation) {
                addExecutable(opName, pmlStmtsOperation.getSignature());
            } else {
                addExecutable(opName, new PMLOperationWrapper(operation).getSignature());
            }
        }

        // same for routines
        Collection<String> routineNames = pap.query().routines().getAdminRoutineNames();
        for (String routineName : routineNames) {
            Routine<?> routine = pap.query().routines().getAdminRoutine(routineName);
            if (routine instanceof PMLStmtsRoutine pmlStmtsRoutine) {
                addExecutable(routineName, pmlStmtsRoutine.getSignature());
            } else {
                addExecutable(routineName, new PMLRoutineWrapper(routine).getSignature());
            }
        }
    }
}
