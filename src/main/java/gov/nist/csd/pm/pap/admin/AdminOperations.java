package gov.nist.csd.pm.pap.admin;

import gov.nist.csd.pm.common.exception.AdminOperationDoesNotExistException;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.op.graph.*;
import gov.nist.csd.pm.pap.function.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.pap.function.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.pap.function.op.operation.CreateAdminOperationOp;
import gov.nist.csd.pm.pap.function.op.operation.DeleteAdminOperationOp;
import gov.nist.csd.pm.pap.function.op.operation.SetResourceOperationsOp;
import gov.nist.csd.pm.pap.function.op.prohibition.CreateProhibitionOp;
import gov.nist.csd.pm.pap.function.op.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.pap.function.op.routine.CreateAdminRoutineOp;
import gov.nist.csd.pm.pap.function.op.routine.DeleteAdminRoutineOp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminOperations {

    public static List<Operation<?, ?>> ADMIN_OPERATIONS = List.of(
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
            new SetResourceOperationsOp(),

            new CreateProhibitionOp(),
            new DeleteProhibitionOp(),

            new CreateAdminRoutineOp(),
            new DeleteAdminRoutineOp()
    );

    public static Set<String> ADMIN_OP_NAMES = new HashSet<>(adminOperationNames());

    public static boolean isAdminOperation(String opName) {
        return ADMIN_OP_NAMES.contains(opName);
    }

    public static Operation<?, ?> get(String opName) throws AdminOperationDoesNotExistException {
        for (Operation<?, ?> op : ADMIN_OPERATIONS) {
            if (op.getName().equals(opName)) {
                return op;
            }
        }

        throw new AdminOperationDoesNotExistException(opName);
    }

    private static Set<String> adminOperationNames() {
        Set<String> names = new HashSet<>();
        for (Operation<?, ?> op : ADMIN_OPERATIONS) {
            names.add(op.getName());
        }

        return names;
    }
}
