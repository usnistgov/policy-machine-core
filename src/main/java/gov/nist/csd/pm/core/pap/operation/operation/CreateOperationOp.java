package gov.nist.csd.pm.core.pap.operation.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.AdminOperationType;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilege;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnNode;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import java.util.List;

public class CreateOperationOp extends AdminOperation<Void>  {

    public static final FormalParameter<Operation<?>> OPERATION_PARAM = new FormalParameter<>("operation", new AdminOperationType());

    public CreateOperationOp() {
        super(
            "create_operation",
            BasicTypes.VOID_TYPE,
            List.of(OPERATION_PARAM),
            AdminPolicyNode.PM_ADMIN_OPERATIONS,
            AdminAccessRight.ADMIN_OPERATION_CREATE
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        // check the user has privileges to query the access on any nodes referenced in req caps
        Operation<?> operation = args.get(OPERATION_PARAM);
        List<RequiredCapability> requiredCapabilities = operation.getRequiredCapabilities();
        for (RequiredCapability reqCap : requiredCapabilities) {
            for (RequiredPrivilege reqPriv : reqCap.getRequiredPrivileges()) {
                if (!(reqPriv instanceof RequiredPrivilegeOnNode requiredPrivilegeOnNode)) {
                    continue;
                }

                long nodeId = pap.query().graph().getNodeByName(requiredPrivilegeOnNode.getName()).getId();
                AccessRightSet privs = pap.query().access()
                    .computePrivileges(userCtx, new TargetContext(nodeId));
                if (!privs.contains(AdminAccessRight.ADMIN_ACCESS_QUERY.toString())) {
                    throw UnauthorizedException.of(pap.query().graph(), userCtx, getName());
                }
            }
        }

        super.canExecute(pap, userCtx, args);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().operations().createOperation(args.get(OPERATION_PARAM));
        return null;
    }

}
