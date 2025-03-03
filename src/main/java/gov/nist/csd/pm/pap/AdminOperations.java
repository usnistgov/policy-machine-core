package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.exception.AdminOperationDoesNotExistException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.common.op.graph.*;
import gov.nist.csd.pm.common.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.common.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.common.op.operation.CreateAdminOperationOp;
import gov.nist.csd.pm.common.op.operation.DeleteAdminOperationOp;
import gov.nist.csd.pm.common.op.operation.SetResourceOperationsOp;
import gov.nist.csd.pm.common.op.prohibition.CreateProhibitionOp;
import gov.nist.csd.pm.common.op.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.common.op.routine.CreateAdminRoutineOp;
import gov.nist.csd.pm.common.op.routine.DeleteAdminRoutineOp;
import gov.nist.csd.pm.pap.store.OperationsStore;

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
