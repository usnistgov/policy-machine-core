package gov.nist.csd.pm.core.pap.operation.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import java.util.List;

public abstract class CreateNodeOp extends AdminOperation<Long> {

    public static final NodeIdListFormalParameter CREATE_NODE_DESCENDANTS_PARAM =
        new NodeIdListFormalParameter("descendants", AdminAccessRight.ADMIN_GRAPH_NODE_CREATE);

    public CreateNodeOp(String name) {
        super(
            name,
            BasicTypes.LONG_TYPE,
            List.of(NAME_PARAM, CREATE_NODE_DESCENDANTS_PARAM)
        );
    }

    public CreateNodeOp(String name, List<FormalParameter<?>> params) {
        super(
            name,
            BasicTypes.LONG_TYPE,
            params
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
