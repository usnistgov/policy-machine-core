package gov.nist.csd.pm.core.pap.function.op.graph;


import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_USER_ATTRIBUTE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeIdListFormalParameter;
import java.util.List;

public class CreateUserAttributeOp extends AdminOperation<Long> {

    public static final NodeIdListFormalParameter CREATE_UA_DESCENDANTS_PARAM =
        new NodeIdListFormalParameter("descendants", CREATE_USER_ATTRIBUTE);

    public CreateUserAttributeOp() {
        super(
            "create_user_attribute",
            BasicTypes.LONG_TYPE,
            List.of(NAME_PARAM, CREATE_UA_DESCENDANTS_PARAM)
        );
    }

    @Override
    public Long execute(PAP pap, Args args) throws PMException {
        String name = args.get(NAME_PARAM);
        List<Long> descIds = args.get(CREATE_UA_DESCENDANTS_PARAM);

        return pap.modify().graph().createUserAttribute(name, descIds);
    }
}