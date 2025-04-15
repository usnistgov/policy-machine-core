package gov.nist.csd.pm.pap.pml.scope;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.compiler.Variable;

import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.pap.pml.function.operation.PMLOperationSignature;
import gov.nist.csd.pm.pap.pml.function.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.function.routine.PMLStmtsRoutine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.PM_ADMIN_OBJECT;
import static gov.nist.csd.pm.pap.admin.AdminPolicyNode.PM_ADMIN_PC;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.ANY_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static gov.nist.csd.pm.pap.pml.function.basic.builtin.PMLBuiltinFunctions.builtinFunctions;

public class CompileScope extends Scope<Variable, PMLFunctionSignature> {

    public CompileScope() {
        Map<String, Variable> constants = new HashMap<>();

        // admin policy nodes constants
        constants.put(PM_ADMIN_PC.constantName(), new Variable(PM_ADMIN_PC.constantName(), STRING_TYPE, true));
        constants.put(PM_ADMIN_OBJECT.constantName(), new Variable(PM_ADMIN_OBJECT.constantName(), STRING_TYPE, true));
        setConstants(constants);

        // add builtin operations
        Map<String, PMLFunctionSignature> functions = new HashMap<>();
        Map<String, PMLBasicFunction> funcs = builtinFunctions();
        for (Map.Entry<String, PMLBasicFunction> func : funcs.entrySet()) {
            functions.put(func.getKey(), func.getValue().getSignature());
        }
        setFunctions(functions);
    }

    public CompileScope(PAP pap) throws PMException {
        // add constants
        Map<String, Variable> constants = new HashMap<>();
        constants.put(PM_ADMIN_PC.constantName(), new Variable(PM_ADMIN_PC.nodeName(), STRING_TYPE, true));
        constants.put(PM_ADMIN_OBJECT.constantName(), new Variable(PM_ADMIN_OBJECT.nodeName(), STRING_TYPE, true));
        setConstants(constants);

        // add pml operations and routines stored in PAP
        Map<String, PMLFunctionSignature> functions = new HashMap<>();
        for (Map.Entry<String, PMLBasicFunction> e : builtinFunctions().entrySet()) {
            functions.put(e.getKey(), e.getValue().getSignature());
        }
        setFunctions(functions);

        // add custom operations from the PAP, could be PML or not PML based
        Collection<String> opNames = pap.query().operations().getAdminOperationNames();
        for (String opName : opNames) {
            Operation<?, ?> operation = pap.query().operations().getAdminOperation(opName);
            if (operation instanceof PMLStmtsOperation pmlStmtsOperation) {
                addFunction(opName, pmlStmtsOperation.getSignature());
            } else {
                addFunction(opName, new PMLOperationSignature(
                    operation.getName(),
                    ANY_TYPE,
                    operation.getFormalArgs()
                ));
            }
        }

        // same for routines
        Collection<String> routineNames = pap.query().routines().getAdminRoutineNames();
        for (String routineName : routineNames) {
            Routine<?, ?> routine = pap.query().routines().getAdminRoutine(routineName);
            if (routine instanceof PMLStmtsRoutine pmlStmtsRoutine) {
                addFunction(routineName, pmlStmtsRoutine.getSignature());
            } else {
                addFunction(routineName, new PMLOperationSignature(
                    routine.getName(),
                    ANY_TYPE,
                    routine.getFormalArgs()
                ));
            }
        }
    }
}
