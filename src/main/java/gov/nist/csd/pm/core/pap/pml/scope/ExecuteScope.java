package gov.nist.csd.pm.core.pap.pml.scope;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.BasicFunction;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.pml.operation.builtin.PMLBuiltinOperations;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ExecuteScope extends Scope<Object, Operation<?>> {

    public ExecuteScope(PAP pap) throws PMException {
        super(pap, loadConstants(), loadOperations(pap));
    }

    private ExecuteScope(PAP pap,
                        Map<String, Object> constants,
                        Map<String, Object> variables,
                        Map<String, Operation<?>> functions,
                        Scope<Object, Operation<?>> parentScope) {
        super(pap, constants, variables, functions, parentScope);
    }

    @Override
    public Scope<Object, Operation<?>> copy() {
        return new ExecuteScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            new HashMap<>(getOperations()),
            getParentScope() != null ? getParentScope().copy() : null
        );
    }

    @Override
    public Scope<Object, Operation<?>> copyBasicFunctionsOnly() {
        Map<String, Operation<?>> basicOnlyFunctions = new HashMap<>();
        for (Operation<?> operation : getOperations().values()) {
            if (!(operation instanceof BasicFunction<?> basicFunction)) {
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
    public Scope<Object, Operation<?>> copyBasicAndQueryFunctionsOnly() {
        Map<String, Operation<?>> filteredFunctions = new HashMap<>();
        for (Operation<?> function : getOperations().values()) {
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

    private static Map<String, Operation<?>> loadOperations(PAP pap) throws PMException {
        // add pml operations and routines stored in PAP
        Map<String, Operation<?>> operationsMap = new HashMap<>(PMLBuiltinOperations.builtinOperations());

        Collection<Operation<?>> operations = pap.query().operations().getOperations();
        for (Operation<?> operation : operations) {
            operationsMap.put(operation.getName(), operation);
        }

        return operationsMap;
    }
}
