package gov.nist.csd.pm.core.pap.operation.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import java.util.List;
import java.util.Map;

public class SetNodePropertiesOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter SET_NODE_PROPS_NODE_ID_PARAM =
        new NodeIdFormalParameter("id");

    public SetNodePropertiesOp() {
        super(
            "set_node_properties",
            BasicTypes.VOID_TYPE,
            List.of(SET_NODE_PROPS_NODE_ID_PARAM, PROPERTIES_PARAM),
            new RequiredCapability(
                SET_NODE_PROPS_NODE_ID_PARAM, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_NODE_UPDATE)
            )
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().graph().setNodeProperties(
            args.get(SET_NODE_PROPS_NODE_ID_PARAM),
            args.get(PROPERTIES_PARAM)
        );

        return null;
    }
}