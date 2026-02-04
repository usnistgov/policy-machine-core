package gov.nist.csd.pm.core.pap.operation.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.AdminOperationType;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class CreateOperationOp extends AdminOperation<Void>  {

    public static final FormalParameter<Operation<?>> OPERATION_PARAM = new FormalParameter<>("operation", new AdminOperationType());

    public CreateOperationOp() {
        super(
            "create_operation",
            BasicTypes.VOID_TYPE,
            List.of(OPERATION_PARAM)
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OPERATIONS.nodeId(),
            AdminAccessRight.ADMIN_OPERATION_CREATE.toString());
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().operations().createOperation(args.get(OPERATION_PARAM));
        return null;
    }

}
