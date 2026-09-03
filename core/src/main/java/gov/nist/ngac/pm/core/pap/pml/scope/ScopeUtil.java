/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.scope;

import gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.ngac.pm.core.pap.operation.AdminOperation;
import gov.nist.ngac.pm.core.pap.operation.Function;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.QueryOperation;
import gov.nist.ngac.pm.core.pap.operation.ResourceOperation;
import gov.nist.ngac.pm.core.pap.operation.Routine;
import gov.nist.ngac.pm.core.pap.pml.compiler.Variable;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import java.util.HashMap;
import java.util.Map;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

/**
 * Seeding logic shared by {@link CompileScope} and {@link NarrowCompileScope}.
 */
final class ScopeUtil {

    private ScopeUtil() {
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
