package gov.nist.csd.pm.core.pap.pml.scope;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminOperations;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.BasicFunction;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.ResourceOperation;
import gov.nist.csd.pm.core.pap.operation.Routine;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import gov.nist.csd.pm.core.pap.pml.operation.builtin.PMLBuiltinFunctions;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompileScope extends Scope<Variable, PMLOperationSignature> {

    public CompileScope(PAP pap) throws PMException {
        super(pap, loadConstants(), loadFunctions(pap));
    }

    private CompileScope(PAP pap,
                         Map<String, Variable> constants,
                         Map<String, Variable> variables,
                         Map<String, PMLOperationSignature> functions,
                         Scope<Variable, PMLOperationSignature> parentScope) {
        super(pap, constants, variables, functions, parentScope);
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
    public CompileScope copyBasicFunctionsOnly() {
        Map<String, PMLOperationSignature> basicOnlyFunctions = new HashMap<>();
        for (PMLOperationSignature op : getOperations().values()) {
            if (!(op.getType() == OperationType.FUNCTION)) {
                continue;
            }

            basicOnlyFunctions.put(op.getName(), op);
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
        Map<String, PMLOperationSignature> filteredFunctions = new HashMap<>();
        for (PMLOperationSignature function : getOperations().values()) {
            if (function.getType() == OperationType.FUNCTION || function.getType() == OperationType.QUERY) {
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

    private static Map<String, PMLOperationSignature> loadFunctions(PAP pap) throws PMException {
        Map<String, PMLOperationSignature> functions = new HashMap<>();

        // add builtin operations and routines stored in PAP
        Map<String, Operation<?>> builtinFuncs = PMLBuiltinFunctions.builtinFunctions();
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

        opNames = pap.query().operations().getAdminRoutineNames();
        for (String opName : opNames) {
            Routine<?> operation = pap.query().operations().getAdminRoutine(opName);
            functions.put(opName, getFunctionSignature(operation));
        }

        opNames = pap.query().operations().getQueryOperationNames();
        for (String opName : opNames) {
            QueryOperation<?> operation = pap.query().operations().getQueryOperation(opName);
            functions.put(opName, getFunctionSignature(operation));
        }

        opNames = pap.query().operations().getBasicFunctionNames();
        for (String opName : opNames) {
            BasicFunction<?> operation = pap.query().operations().getBasicFunction(opName);
            functions.put(opName, getFunctionSignature(operation));
        }

        // basic functions and query functions from plugin registry
        List<BasicFunction<?>> basicFunctionNames = pap.plugins().getBasicFunctions();
        for (BasicFunction<?> basicFunction : basicFunctionNames) {
            functions.put(basicFunction.getName(), getFunctionSignature(basicFunction));
        }

        List<QueryOperation<?>> queryOperationNames = pap.plugins().getQueryOperations();
        for (QueryOperation<?> queryFunc : queryOperationNames) {
            functions.put(queryFunc.getName(), getFunctionSignature(queryFunc));
        }

        return functions;
    }

    private static PMLOperationSignature getFunctionSignature(Operation<?> func) {
        return switch (func) {
            case BasicFunction<?> basicFunction -> new PMLOperationSignature(
                OperationType.FUNCTION, func.getName(), func.getReturnType(), func.getFormalParameters()
            );
            case QueryOperation<?> queryOperation -> new PMLOperationSignature(
                OperationType.QUERY, func.getName(), func.getReturnType(), func.getFormalParameters()
            );
            case AdminOperation<?> adminOperation -> new PMLOperationSignature(
                OperationType.ADMINOP, func.getName(), func.getReturnType(), func.getFormalParameters()
            );
            case Routine<?> routine -> new PMLOperationSignature(
                OperationType.ROUTINE, func.getName(), func.getReturnType(), func.getFormalParameters()
            );
            case ResourceOperation<?> resourceOperation -> new PMLOperationSignature(
                OperationType.RESOURCEOP, func.getName(), func.getReturnType(), func.getFormalParameters()
            );
        };
    }
}
