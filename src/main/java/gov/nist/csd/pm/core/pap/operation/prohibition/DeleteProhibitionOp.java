package gov.nist.csd.pm.core.pap.operation.prohibition;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnParameter;
import java.util.List;

public class DeleteProhibitionOp extends ProhibitionOp {

    public DeleteProhibitionOp() {
        super(
            "delete_prohibition",
            List.of(NAME_PARAM, NODE_ID_PARAM, INCLUSION_SET_PARAM, EXCLUSION_SET_PARAM),
            new RequiredCapability(
                new RequiredPrivilegeOnParameter(NODE_ID_PARAM, AdminAccessRight.ADMIN_PROHIBITION_NODE_DELETE),
                new RequiredPrivilegeOnParameter(INCLUSION_SET_PARAM, AdminAccessRight.ADMIN_PROHIBITION_INCLUSION_DELETE),
                new RequiredPrivilegeOnParameter(EXCLUSION_SET_PARAM, AdminAccessRight.ADMIN_PROHIBITION_EXCLUSION_DELETE)
            )
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().prohibitions().deleteProhibition(
            args.get(NAME_PARAM)
        );
        return null;
    }
}
