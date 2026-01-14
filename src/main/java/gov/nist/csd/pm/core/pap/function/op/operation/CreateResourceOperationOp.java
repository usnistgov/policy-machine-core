package gov.nist.csd.pm.core.pap.function.op.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.ResourceOperationType;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.op.ResourceOperation;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_RESOURCE_OPERATION;

public class CreateResourceOperationOp extends AdminOperation<Void> {

    public static final FormalParameter<ResourceOperation> OPERATION_PARAM =
        new FormalParameter<>("operation", new ResourceOperationType());

    public CreateResourceOperationOp() {
        super(
            "create_resource_operation",
            List.of(OPERATION_PARAM)
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OPERATIONS.nodeId(), CREATE_RESOURCE_OPERATION);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().operations().createResourceOperation(args.get(OPERATION_PARAM));
        return null;
    }
}
