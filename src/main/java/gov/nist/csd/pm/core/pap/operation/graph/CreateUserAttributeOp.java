package gov.nist.csd.pm.core.pap.operation.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import java.util.List;

public class CreateUserAttributeOp extends AdminOperation<Long> {

    public static final NodeIdListFormalParameter CREATE_UA_DESCENDANTS_PARAM =
        new NodeIdListFormalParameter("descendants", AdminAccessRight.ADMIN_GRAPH_NODE_UA_CREATE);

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