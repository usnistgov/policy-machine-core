package gov.nist.csd.pm.core.pap.function.op.operation;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_ADMIN_OPERATION;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.AdminOperationType;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class CreateAdminOperationOp extends AdminOperation<Void> {

    public static final FormalParameter<AdminOperation<?>> OPERATION_PARAM = new FormalParameter<>("operation", new AdminOperationType());

    public CreateAdminOperationOp() {
        super(
                "create_admin_operation",
            BasicTypes.VOID_TYPE,
                List.of(OPERATION_PARAM)
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OPERATIONS.nodeId(), CREATE_ADMIN_OPERATION);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().operations().createAdminOperation(args.get(OPERATION_PARAM));
        return null;
    }
}
