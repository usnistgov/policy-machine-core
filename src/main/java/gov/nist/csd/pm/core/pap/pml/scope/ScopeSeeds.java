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
        return new PMLOperationSignature(
            operationTypeOf(op), op.getName(), op.getReturnType(), op.getFormalParameters(),
            op.getEventParameters(), op.getRequiredCapabilities());
    }

    private static OperationType operationTypeOf(Operation<?> op) {
        return switch (op) {
            case Function<?> function -> OperationType.FUNCTION;
            case QueryOperation<?> queryOperation -> OperationType.QUERY;
            case AdminOperation<?> adminOperation -> OperationType.ADMINOP;
            case Routine<?> routine -> OperationType.ROUTINE;
            case ResourceOperation<?> resourceOperation -> OperationType.RESOURCEOP;
        };
    }

    static boolean isFunction(PMLOperationSignature signature) {
        return signature.getType() == OperationType.FUNCTION;
    }

    static boolean isFunctionOrQuery(PMLOperationSignature signature) {
        return signature.getType() == OperationType.FUNCTION || signature.getType() == OperationType.QUERY;
    }
}
