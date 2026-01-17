package gov.nist.csd.pm.core.pap.admin;

import gov.nist.csd.pm.core.common.exception.AdminOperationDoesNotExistException;
import gov.nist.csd.pm.core.pap.function.Operation;
import gov.nist.csd.pm.core.pap.function.op.graph.AssignOp;
import gov.nist.csd.pm.core.pap.function.op.graph.AssociateOp;
import gov.nist.csd.pm.core.pap.function.op.graph.CreateObjectAttributeOp;
import gov.nist.csd.pm.core.pap.function.op.graph.CreateObjectOp;
import gov.nist.csd.pm.core.pap.function.op.graph.CreatePolicyClassOp;
import gov.nist.csd.pm.core.pap.function.op.graph.CreateUserAttributeOp;
import gov.nist.csd.pm.core.pap.function.op.graph.CreateUserOp;
import gov.nist.csd.pm.core.pap.function.op.graph.DeassignOp;
import gov.nist.csd.pm.core.pap.function.op.graph.DeleteNodeOp;
import gov.nist.csd.pm.core.pap.function.op.graph.DissociateOp;
import gov.nist.csd.pm.core.pap.function.op.graph.SetNodePropertiesOp;
import gov.nist.csd.pm.core.pap.function.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.core.pap.function.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.core.pap.function.op.operation.CreateAdminOperationOp;
import gov.nist.csd.pm.core.pap.function.op.operation.CreateResourceOperationOp;
import gov.nist.csd.pm.core.pap.function.op.operation.DeleteAdminOperationOp;
import gov.nist.csd.pm.core.pap.function.op.operation.DeleteResourceOperationOp;
import gov.nist.csd.pm.core.pap.function.op.operation.SetResourceAccessRights;
import gov.nist.csd.pm.core.pap.function.op.prohibition.CreateProhibitionOp;
import gov.nist.csd.pm.core.pap.function.op.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.core.pap.function.op.routine.CreateAdminRoutineOp;
import gov.nist.csd.pm.core.pap.function.op.routine.DeleteAdminRoutineOp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminOperations {

    public static List<Operation<?>> ADMIN_OPERATIONS = List.of(
            new AssignOp(),
            new AssociateOp(),
            new CreateObjectAttributeOp(),
            new CreateObjectOp(),
            new CreatePolicyClassOp(),
            new CreateUserAttributeOp(),
            new CreateUserOp(),
            new DeassignOp(),
            new DeleteNodeOp(),
            new DissociateOp(),
            new SetNodePropertiesOp(),

            new CreateObligationOp(),
            new DeleteObligationOp(),

            new CreateAdminOperationOp(),
            new DeleteAdminOperationOp(),
            new SetResourceAccessRights(),
            new CreateResourceOperationOp(),
            new DeleteResourceOperationOp(),

            new CreateProhibitionOp(),
            new DeleteProhibitionOp(),

            new CreateAdminRoutineOp(),
            new DeleteAdminRoutineOp()
    );

    public static Set<String> ADMIN_OP_NAMES = new HashSet<>(adminOperationNames());

    public static boolean isAdminOperation(String opName) {
        return ADMIN_OP_NAMES.contains(opName);
    }

    public static Operation<?> get(String opName) throws AdminOperationDoesNotExistException {
        for (Operation<?> op : ADMIN_OPERATIONS) {
            if (op.getName().equals(opName)) {
                return op;
            }
        }

        throw new AdminOperationDoesNotExistException(opName);
    }

    private static Set<String> adminOperationNames() {
        Set<String> names = new HashSet<>();
        for (Operation<?> op : ADMIN_OPERATIONS) {
            names.add(op.getName());
        }

        return names;
    }
}
