package gov.nist.csd.pm.core.pap.pml.scope;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminOperations;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.BasicFunction;
import gov.nist.csd.pm.core.pap.function.Function;
import gov.nist.csd.pm.core.pap.function.Operation;
import gov.nist.csd.pm.core.pap.function.QueryOperation;
import gov.nist.csd.pm.core.pap.function.Routine;
import gov.nist.csd.pm.core.pap.pml.function.basic.builtin.PMLBuiltinFunctions;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecuteScope extends Scope<Object, Function<?>> {

    public ExecuteScope(PAP pap) throws PMException {
        super(pap, loadConstants(), loadFunctions(pap));
    }

    private ExecuteScope(PAP pap,
                        Map<String, Object> constants,
                        Map<String, Object> variables,
                        Map<String, Function<?>> functions,
                        Scope<Object, Function<?>> parentScope) {
        super(pap, constants, variables, functions, parentScope);
    }

    @Override
    public Scope<Object, Function<?>> copy() {
        return new ExecuteScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            new HashMap<>(getFunctions()),
            getParentScope() != null ? getParentScope().copy() : null
        );
    }

    @Override
    public Scope<Object, Function<?>> copyBasicFunctionsOnly() {
        Map<String, Function<?>> basicOnlyFunctions = new HashMap<>();
        for (Function<?> function : getFunctions().values()) {
            if (!(function instanceof BasicFunction<?> basicFunction)) {
                continue;
            }

            basicOnlyFunctions.put(basicFunction.getName(), basicFunction);
        }

        return new ExecuteScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            basicOnlyFunctions,
            getParentScope() != null ? getParentScope().copy() : null
        );
    }

    @Override
    public Scope<Object, Function<?>> copyBasicAndQueryFunctionsOnly() {
        Map<String, Function<?>> filteredFunctions = new HashMap<>();
        for (Function<?> function : getFunctions().values()) {
            if (function instanceof BasicFunction<?> || function instanceof QueryOperation<?>) {
                filteredFunctions.put(function.getName(), function);
            }
        }

        return new ExecuteScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            filteredFunctions,
            getParentScope() != null ? getParentScope().copy() : null
        );
    }

    private static Map<String, Object> loadConstants() {
        Map<String, Object> constants = new HashMap<>();
        for (AdminPolicyNode adminPolicyNode : AdminPolicyNode.values()) {
            constants.put(adminPolicyNode.constantName(), adminPolicyNode.nodeName());
        }

        return constants;
    }

    private static Map<String, Function<?>> loadFunctions(PAP pap) throws PMException {
        // add pml operations and routines stored in PAP
        Map<String, Function<?>> functions = new HashMap<>(PMLBuiltinFunctions.builtinFunctions());

        // add custom operations from the PAP, could be PML or not PML based
        Collection<String> opNames = pap.query().operations().getAdminOperationNames();
        for (String opName : opNames) {
            Operation<?> operation = pap.query().operations().getAdminOperation(opName);
            functions.put(opName, operation);
        }

        opNames = pap.query().operations().getResourceOperationNames();
        for (String opName : opNames) {
            Operation<?> operation = pap.query().operations().getResourceOperation(opName);
            functions.put(opName, operation);
        }

        // admin ops
        for (Operation<?> adminOperation : AdminOperations.ADMIN_OPERATIONS) {
            functions.put(adminOperation.getName(), adminOperation);
        }

        // same for routines
        Collection<String> routineNames = pap.query().operations().getAdminRoutineNames();
        for (String routineName : routineNames) {
            Routine<?> routine = pap.query().operations().getAdminRoutine(routineName);
            functions.put(routineName, routine);
        }

        // basic functions and query funcs from plugin registry
        List<String> basicFunctionNames = pap.plugins().getBasicFunctionNames();
        for (String basicFunctionName : basicFunctionNames) {
            BasicFunction<?> function = pap.plugins().getBasicFunction(basicFunctionName);
            functions.put(basicFunctionName, function);
        }

        List<QueryOperation<?>> queryOperationNames = pap.plugins().getQueryOperations();
        for (QueryOperation<?> queryFunc : queryOperationNames) {
            functions.put(queryFunc.getName(), queryFunc);
        }

        return functions;
    }
}
