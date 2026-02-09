package gov.nist.csd.pm.core.pap.operation.prohibition;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnParameter;
import java.util.HashSet;
import java.util.List;

public class CreateProcessProhibitionOp extends ProhibitionOp {

    public static NodeIdFormalParameter USER_ID_PARAM = new NodeIdFormalParameter("user_id");
    public static FormalParameter<String> PROCESS_PARAM = new FormalParameter<>("process", STRING_TYPE);

    public CreateProcessProhibitionOp() {
        super(
            "create_process_prohibition",
            List.of(NAME_PARAM, USER_ID_PARAM, PROCESS_PARAM, ARSET_PARAM, INCLUSION_SET_PARAM, EXCLUSION_SET_PARAM, IS_CONJUNCTIVE_PARAM),
            new RequiredCapability(
                new RequiredPrivilegeOnParameter(USER_ID_PARAM, AdminAccessRight.ADMIN_PROHIBITION_PROCESS_CREATE),
                new RequiredPrivilegeOnParameter(INCLUSION_SET_PARAM, AdminAccessRight.ADMIN_PROHIBITION_INCLUSION_CREATE),
                new RequiredPrivilegeOnParameter(EXCLUSION_SET_PARAM, AdminAccessRight.ADMIN_PROHIBITION_EXCLUSION_CREATE)
            )
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().prohibitions().createProcessProhibition(
            args.get(NAME_PARAM),
            args.get(USER_ID_PARAM),
            args.get(PROCESS_PARAM),
            new AccessRightSet(args.get(ARSET_PARAM)),
            new HashSet<>(args.get(INCLUSION_SET_PARAM)),
            new HashSet<>(args.get(EXCLUSION_SET_PARAM)),
            args.get(IS_CONJUNCTIVE_PARAM)
        );
        return null;
    }
}
