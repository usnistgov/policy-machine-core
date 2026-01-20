package gov.nist.csd.pm.core.pap.operation.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminAccessRights;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.BasicFunction;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicFunctionType;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class CreateBasicFunctionOp extends AdminOperation<Void> {

    public static FormalParameter<BasicFunction<?>> BASIC_FUNCTION_PARAM =
        new FormalParameter<>("function", new BasicFunctionType());

    public CreateBasicFunctionOp() {
        super("create_basic_function", VOID_TYPE, List.of(BASIC_FUNCTION_PARAM));
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        pap.privilegeChecker().check(userCtx, new TargetContext(AdminPolicyNode.PM_ADMIN_OPERATIONS.nodeId()),
            AdminAccessRights.CREATE_BASIC_FUNCTION);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().operations().createBasicFunction(args.get(BASIC_FUNCTION_PARAM));
        return null;
    }
}
