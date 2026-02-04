package gov.nist.csd.pm.core.pap.operation.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import java.util.List;

public class CreateUserOp extends AdminOperation<Long> {

    public static final NodeIdListFormalParameter CREATE_U_DESCENDANTS_PARAM =
        new NodeIdListFormalParameter("descendants", AdminAccessRight.ADMIN_GRAPH_NODE_U_CREATE);

    public CreateUserOp() {
        super(
            "create_user",
            BasicTypes.LONG_TYPE,
            List.of(NAME_PARAM, CREATE_U_DESCENDANTS_PARAM)
        );
    }

    @Override
    public Long execute(PAP pap, Args args) throws PMException {
        String name = args.get(NAME_PARAM);
        List<Long> descIds = args.get(CREATE_U_DESCENDANTS_PARAM);

        return pap.modify().graph().createUser(name, descIds);
    }
}
