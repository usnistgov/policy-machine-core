package gov.nist.csd.pm.core.pap.operation.prohibition;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnParameter;
import java.util.HashSet;
import java.util.List;

public class CreateNodeProhibitionOp extends ProhibitionOp {

    public CreateNodeProhibitionOp() {
        super(
            "create_node_prohibition",
            List.of(NAME_PARAM, NODE_ID_PARAM, ARSET_PARAM, INCLUSION_SET_PARAM, EXCLUSION_SET_PARAM, IS_CONJUNCTIVE_PARAM),
            new RequiredCapability(
                new RequiredPrivilegeOnParameter(NODE_ID_PARAM, AdminAccessRight.ADMIN_PROHIBITION_NODE_CREATE),
                new RequiredPrivilegeOnParameter(INCLUSION_SET_PARAM, AdminAccessRight.ADMIN_PROHIBITION_INCLUSION_CREATE),
                new RequiredPrivilegeOnParameter(EXCLUSION_SET_PARAM, AdminAccessRight.ADMIN_PROHIBITION_EXCLUSION_CREATE)
            )
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().prohibitions().createNodeProhibition(
            args.get(NAME_PARAM),
            args.get(NODE_ID_PARAM),
            new AccessRightSet(args.get(ARSET_PARAM)),
            new HashSet<>(args.get(INCLUSION_SET_PARAM)),
            new HashSet<>(args.get(EXCLUSION_SET_PARAM)),
            args.get(IS_CONJUNCTIVE_PARAM)
        );

        return null;
    }
}
