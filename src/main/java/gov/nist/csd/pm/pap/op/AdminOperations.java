package gov.nist.csd.pm.pap.op;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.op.graph.*;
import gov.nist.csd.pm.pap.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.pap.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.pap.op.operation.CreateAdminOperationOp;
import gov.nist.csd.pm.pap.op.operation.DeleteAdminOperationOp;
import gov.nist.csd.pm.pap.op.operation.SetResourceOperationsOp;
import gov.nist.csd.pm.pap.op.prohibition.CreateProhibitionOp;
import gov.nist.csd.pm.pap.op.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.pap.op.routine.CreateAdminRoutineOp;
import gov.nist.csd.pm.pap.op.routine.DeleteAdminRoutineOp;
import gov.nist.csd.pm.pap.store.OperationsStore;

import java.util.*;

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
            new DeleteObjectAttributeOp(),
            new DeleteObjectOp(),
            new DeletePolicyClassOp(),
            new DeleteUserAttributeOp(),
            new DeleteUserOp(),
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

    public static void init(OperationsStore operationsStore) throws PMException {
        for (Operation<?> op : ADMIN_OPERATIONS) {
            operationsStore.createAdminOperation(op);
        }
    }

    private static Set<String> adminOperationNames() {
        Set<String> names = new HashSet<>();
        for (Operation<?> op : ADMIN_OPERATIONS) {
            names.add(op.getName());
        }

        return names;
    }
}
