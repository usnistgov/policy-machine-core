package gov.nist.csd.pm.core.pap.pml.scope;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.Function;
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
                        Map<String, Operation<?>> operations,
                        Scope<Object, Operation<?>> parentScope) {
        super(pap, constants, variables, operations, parentScope);
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
    public Scope<Object, Operation<?>> copyFunctionsOnly() {
        Map<String, Operation<?>> filteredOps = new HashMap<>();
        for (Operation<?> operation : getOperations().values()) {
            if (!(operation instanceof Function<?> function)) {
                continue;
            }

            filteredOps.put(function.getName(), function);
        }

        return new ExecuteScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            filteredOps,
            getParentScope() != null ? getParentScope().copy() : null
        );
    }

    @Override
    public Scope<Object, Operation<?>> copyFunctionsAndQueriesOnly() {
        Map<String, Operation<?>> filteredOps = new HashMap<>();
        for (Operation<?> operation : getOperations().values()) {
            if (operation instanceof Function<?> || operation instanceof QueryOperation<?>) {
                filteredOps.put(operation.getName(), operation);
            }
        }

        return new ExecuteScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            filteredOps,
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
