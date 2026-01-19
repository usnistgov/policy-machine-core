package gov.nist.csd.pm.core.pap.function.op.operation;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.SET_RESOURCE_ACCESS_RIGHTS;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class SetResourceAccessRights extends AdminOperation<Void> {

    public SetResourceAccessRights() {
        super(
                "set_resource_access_rights",
            BasicTypes.VOID_TYPE,
                List.of(ARSET_PARAM)
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OPERATIONS.nodeId(), SET_RESOURCE_ACCESS_RIGHTS);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().operations().setResourceAccessRights(new AccessRightSet(args.get(ARSET_PARAM)));
        return null;
    }
}
