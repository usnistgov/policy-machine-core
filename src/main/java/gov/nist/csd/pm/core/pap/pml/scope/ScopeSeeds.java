package gov.nist.csd.pm.core.pap.pml.scope;

import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.Function;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.ResourceOperation;
import gov.nist.csd.pm.core.pap.operation.Routine;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import java.util.HashMap;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

/**
 * Seeding logic shared by {@link CompileScope} and {@link NarrowCompileScope} — the two scopes differ only
 * in how eagerly/broadly they populate their operations map, not in how a single constant or operation
 * signature is built.
 */
final class ScopeSeeds {

    private ScopeSeeds() {
    }

    static Map<String, Variable> loadConstants() {
        Map<String, Variable> constants = new HashMap<>();
        for (AdminPolicyNode adminPolicyNode : AdminPolicyNode.values()) {
            constants.put(adminPolicyNode.constantName(), new Variable(adminPolicyNode.constantName(), STRING_TYPE, true));
        }

        return constants;
    }

    static PMLOperationSignature createOperationSignature(Operation<?> op) {
        return switch (op) {
            case Function<?> function -> new PMLOperationSignature(
                OperationType.FUNCTION, op.getName(), op.getReturnType(), op.getFormalParameters(),
                function.getRequiredCapabilities());
            case QueryOperation<?> queryOperation -> new PMLOperationSignature(
                OperationType.QUERY, op.getName(), op.getReturnType(), op.getFormalParameters(),
                queryOperation.getRequiredCapabilities());
            case AdminOperation<?> adminOperation -> new PMLOperationSignature(
                OperationType.ADMINOP, op.getName(), op.getReturnType(), op.getFormalParameters(),
                adminOperation.getRequiredCapabilities());
            case Routine<?> routine -> new PMLOperationSignature(
                OperationType.ROUTINE, op.getName(), op.getReturnType(), op.getFormalParameters(),
                routine.getRequiredCapabilities());
            case ResourceOperation<?> resourceOperation -> new PMLOperationSignature(
                OperationType.RESOURCEOP, op.getName(), op.getReturnType(), op.getFormalParameters(),
                resourceOperation.getRequiredCapabilities());
        };
    }

    static boolean isFunction(PMLOperationSignature signature) {
        return signature.getType() == OperationType.FUNCTION;
    }

    static boolean isFunctionOrQuery(PMLOperationSignature signature) {
        return signature.getType() == OperationType.FUNCTION || signature.getType() == OperationType.QUERY;
    }
}
