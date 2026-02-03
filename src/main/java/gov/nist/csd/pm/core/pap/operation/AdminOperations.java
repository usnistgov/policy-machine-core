package gov.nist.csd.pm.core.pap.operation;

import gov.nist.csd.pm.core.common.exception.AdminOperationDoesNotExistException;
import gov.nist.csd.pm.core.pap.operation.graph.AssignOp;
import gov.nist.csd.pm.core.pap.operation.graph.AssociateOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateObjectAttributeOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateObjectOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreatePolicyClassOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateUserAttributeOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateUserOp;
import gov.nist.csd.pm.core.pap.operation.graph.DeassignOp;
import gov.nist.csd.pm.core.pap.operation.graph.DeleteNodeOp;
import gov.nist.csd.pm.core.pap.operation.graph.DissociateOp;
import gov.nist.csd.pm.core.pap.operation.graph.SetNodePropertiesOp;
import gov.nist.csd.pm.core.pap.operation.obligation.CreateObligationOp;
import gov.nist.csd.pm.core.pap.operation.obligation.DeleteObligationOp;
import gov.nist.csd.pm.core.pap.operation.operation.CreateOperationOp;
import gov.nist.csd.pm.core.pap.operation.operation.DeleteOperationOp;
import gov.nist.csd.pm.core.pap.operation.operation.SetResourceAccessRights;
import gov.nist.csd.pm.core.pap.operation.prohibition.CreateProhibitionOp;
import gov.nist.csd.pm.core.pap.operation.prohibition.DeleteProhibitionOp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AdminOperations stores a list of the admin operations used in the PolicyModification interfaces.
 */
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

        new SetResourceAccessRights(),
        new CreateOperationOp(),
        new DeleteOperationOp(),

        new CreateProhibitionOp(),
        new DeleteProhibitionOp()
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
