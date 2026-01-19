package gov.nist.csd.pm.core.pap.pml.scope;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminOperations;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.BasicFunction;
import gov.nist.csd.pm.core.pap.function.Function;
import gov.nist.csd.pm.core.pap.function.Operation;
import gov.nist.csd.pm.core.pap.function.QueryFunction;
import gov.nist.csd.pm.core.pap.function.ResourceOperation;
import gov.nist.csd.pm.core.pap.function.Routine;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.function.basic.builtin.PMLBuiltinFunctions;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLResourceOperation;
import gov.nist.csd.pm.core.pap.pml.function.query.PMLQueryFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.function.routine.PMLRoutineSignature;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompileScope extends Scope<Variable, PMLFunctionSignature> {

    public CompileScope(PAP pap) throws PMException {
        super(pap, loadConstants(), loadFunctions(pap));
    }

    private CompileScope(PAP pap,
                         Map<String, Variable> constants,
                         Map<String, Variable> variables,
                         Map<String, PMLFunctionSignature> functions,
                         Scope<Variable, PMLFunctionSignature> parentScope) {
        super(pap, constants, variables, functions, parentScope);
    }

    @Override
    public CompileScope copy() {
        return new CompileScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            new HashMap<>(getFunctions()),
            this.getParentScope() != null ? this.getParentScope().copy() : null
        );
    }

    @Override
    public CompileScope copyBasicFunctionsOnly() {
        Map<String, PMLFunctionSignature> basicOnlyFunctions = new HashMap<>();
        for (PMLFunctionSignature function : getFunctions().values()) {
            if (!(function instanceof PMLBasicFunctionSignature basicFunction)) {
                continue;
            }

            basicOnlyFunctions.put(basicFunction.getName(), basicFunction);
        }

        return new CompileScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            basicOnlyFunctions,
            getParentScope() != null ? getParentScope().copy() : null
        );
    }

    @Override
    public CompileScope copyBasicAndQueryFunctionsOnly() {
        Map<String, PMLFunctionSignature> filteredFunctions = new HashMap<>();
        for (PMLFunctionSignature function : getFunctions().values()) {
            if (function instanceof PMLBasicFunctionSignature || function instanceof PMLQueryFunctionSignature) {
                filteredFunctions.put(function.getName(), function);
            }
        }

        return new CompileScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            filteredFunctions,
            getParentScope() != null ? getParentScope().copy() : null
        );
    }

    private static Map<String, Variable> loadConstants() {
        Map<String, Variable> constants = new HashMap<>();
        for (AdminPolicyNode adminPolicyNode : AdminPolicyNode.values()) {
            constants.put(adminPolicyNode.constantName(), new Variable(adminPolicyNode.constantName(), STRING_TYPE, true));
        }

        return constants;
    }

    private static Map<String, PMLFunctionSignature> loadFunctions(PAP pap) throws PMException {
        Map<String, PMLFunctionSignature> functions = new HashMap<>();

        // add builtin operations and routines stored in PAP
        Map<String, Function<?>> builtinFuncs = PMLBuiltinFunctions.builtinFunctions();
        builtinFuncs.values().forEach(f -> {
            functions.put(f.getName(), getFunctionSignature(f));
        });

        // add admin ops
        for (Operation<?> adminOperation : AdminOperations.ADMIN_OPERATIONS) {
            functions.put(adminOperation.getName(), getFunctionSignature(adminOperation));
        }

        // add custom operations from the PAP, could be PML or not PML based
        Collection<String> opNames = pap.query().operations().getAdminOperationNames();
        for (String opName : opNames) {
            Operation<?> operation = pap.query().operations().getAdminOperation(opName);
            functions.put(opName, getFunctionSignature(operation));
        }

        opNames = pap.query().operations().getResourceOperationNames();
        for (String opName : opNames) {
            Operation<?> operation = pap.query().operations().getResourceOperation(opName);
            functions.put(opName, getFunctionSignature(operation));
        }

        // same for routines
        Collection<String> routineNames = pap.query().operations().getAdminRoutineNames();
        for (String routineName : routineNames) {
            Routine<?> routine = pap.query().operations().getAdminRoutine(routineName);
            functions.put(routineName, getFunctionSignature(routine));
        }

        // basic functions and query functions from plugin registry
        List<BasicFunction<?>> basicFunctionNames = pap.plugins().getBasicFunctions();
        for (BasicFunction<?> basicFunction : basicFunctionNames) {
            functions.put(basicFunction.getName(), getFunctionSignature(basicFunction));
        }

        List<QueryFunction<?>> queryFunctionNames = pap.plugins().getQueryFunctions();
        for (QueryFunction<?> queryFunc : queryFunctionNames) {
            functions.put(queryFunc.getName(), getFunctionSignature(queryFunc));
        }

        return functions;
    }

    private static PMLFunctionSignature getFunctionSignature(Function<?> func) {
        return switch (func) {
            case BasicFunction<?> basicFunction -> new PMLBasicFunctionSignature(
                func.getName(), func.getReturnType(), func.getFormalParameters()
            );
            case QueryFunction<?> basicFunction -> new PMLQueryFunctionSignature(
                func.getName(), func.getReturnType(), func.getFormalParameters()
            );
            case AdminOperation<?> adminOperation -> new PMLOperationSignature(
                func.getName(), func.getReturnType(), func.getFormalParameters(), true
            );
            case Routine<?> routine -> new PMLRoutineSignature(
                func.getName(), func.getReturnType(), func.getFormalParameters()
            );
            case ResourceOperation<?> resourceOperation -> new PMLOperationSignature(
                func.getName(), func.getReturnType(), func.getFormalParameters(), false
            );
        };
    }
}
