package gov.nist.csd.pm.core.pap.operation.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnParameter;
import java.util.List;

public abstract class CreateNodeOp extends AdminOperation<Long> {

    public static final NodeIdListFormalParameter CREATE_NODE_DESCENDANTS_PARAM = new NodeIdListFormalParameter("descendants");

    public CreateNodeOp(String name) {
        super(
            name,
            BasicTypes.LONG_TYPE,
            List.of(NAME_PARAM, CREATE_NODE_DESCENDANTS_PARAM),
            new RequiredCapability(
                new RequiredPrivilegeOnParameter(CREATE_NODE_DESCENDANTS_PARAM, AdminAccessRight.ADMIN_GRAPH_NODE_CREATE)
            )
        );
    }

    public CreateNodeOp(String name, List<FormalParameter<?>> params, RequiredCapability reqCap) {
        super(
            name,
            BasicTypes.LONG_TYPE,
            params,
            reqCap
        );
    }

    protected abstract long createNode(PAP pap, String name, List<Long> descs) throws PMException;

    @Override
    public Long execute(PAP pap, Args args) throws PMException {
        String name = args.get(NAME_PARAM);
        List<Long> descIds = args.get(CREATE_NODE_DESCENDANTS_PARAM);

        return createNode(pap, name, descIds);
    }
}
