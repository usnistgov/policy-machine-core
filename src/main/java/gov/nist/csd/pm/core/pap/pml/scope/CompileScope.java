package gov.nist.csd.pm.core.pap.pml.scope;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperations;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.builtin.PMLBuiltinOperations;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CompileScope extends Scope<Variable, PMLOperationSignature> {

    public CompileScope(PAP pap) throws PMException {
        super(pap, ScopeSeeds.loadConstants(), loadOperations(pap));
    }

    private CompileScope(PAP pap,
                         Map<String, Variable> constants,
                         Map<String, Variable> variables,
                         Map<String, PMLOperationSignature> operations,
                         Scope<Variable, PMLOperationSignature> parentScope) {
        super(pap, constants, variables, operations, parentScope);
    }

    @Override
    public CompileScope copy() {
        return new CompileScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            new HashMap<>(getOperations()),
            this.getParentScope() != null ? this.getParentScope().copy() : null
        );
    }

    @Override
    public CompileScope copyFunctionsOnly() {
        Map<String, PMLOperationSignature> operations = new HashMap<>();
        for (PMLOperationSignature op : getOperations().values()) {
            if (!ScopeSeeds.isFunction(op)) {
                continue;
            }

            operations.put(op.getName(), op);
        }

        return new CompileScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            operations,
            getParentScope() != null ? getParentScope().copy() : null
        );
    }

    @Override
    public CompileScope copyFunctionsAndQueriesOnly() {
        Map<String, PMLOperationSignature> filteredOps = new HashMap<>();
        for (PMLOperationSignature function : getOperations().values()) {
            if (ScopeSeeds.isFunctionOrQuery(function)) {
                filteredOps.put(function.getName(), function);
            }
        }

        return new CompileScope(
            this.getPap(),
            new HashMap<>(getConstants()),
            new HashMap<>(getVariables()),
            filteredOps,
            getParentScope() != null ? getParentScope().copy() : null
        );
    }

    private static Map<String, PMLOperationSignature> loadOperations(PAP pap) throws PMException {
        Map<String, PMLOperationSignature> operationSignatures = new HashMap<>();

        // add builtin operations and routines stored in PAP
        Map<String, Operation<?>> builtinFuncs = PMLBuiltinOperations.builtinOperations();
        builtinFuncs.values().forEach(f -> {
            operationSignatures.put(f.getName(), ScopeSeeds.createOperationSignature(f));
        });

        Collection<Operation<?>> operations = pap.query().operations().getOperations();
        for (Operation<?> op : operations) {
            operationSignatures.put(op.getName(), ScopeSeeds.createOperationSignature(op));
        }

        // add admin ops
        for (Operation<?> adminOperation : AdminOperations.ADMIN_OPERATIONS) {
            operationSignatures.put(adminOperation.getName(), ScopeSeeds.createOperationSignature(adminOperation));
        }

        return operationSignatures;
    }
}
