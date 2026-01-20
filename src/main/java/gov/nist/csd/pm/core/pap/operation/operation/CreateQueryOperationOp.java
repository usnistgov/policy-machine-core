package gov.nist.csd.pm.core.pap.operation.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminAccessRights;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.arg.type.QueryOperationType;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class CreateQueryOperationOp extends AdminOperation<Void> {

    public static FormalParameter<QueryOperation<?>> QUERY_OPERATION_PARAM =
        new FormalParameter<>("query", new QueryOperationType());

    public CreateQueryOperationOp() {
        super("create_query_operation", VOID_TYPE, List.of(QUERY_OPERATION_PARAM));
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        pap.privilegeChecker().check(userCtx, new TargetContext(AdminPolicyNode.PM_ADMIN_OPERATIONS.nodeId()),
            AdminAccessRights.CREATE_QUERY_OPERATION);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().operations().createQueryOperation(args.get(QUERY_OPERATION_PARAM));
        return null;
    }
}
