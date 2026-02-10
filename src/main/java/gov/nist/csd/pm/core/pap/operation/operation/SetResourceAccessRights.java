package gov.nist.csd.pm.core.pap.operation.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import java.util.List;

public class SetResourceAccessRights extends AdminOperation<Void> {

    public SetResourceAccessRights() {
        super(
            "set_resource_access_rights",
            BasicTypes.VOID_TYPE,
            List.of(ARSET_PARAM),
            AdminPolicyNode.PM_ADMIN_OPERATIONS,
            AdminAccessRight.ADMIN_POLICY_RESOURCE_ACCESS_RIGHTS_UPDATE
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().operations().setResourceAccessRights(new AccessRightSet(args.get(ARSET_PARAM)));
        return null;
    }
}
