package gov.nist.csd.pm.core.pap.operation.graph;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.SET_NODE_PROPERTIES;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import java.util.List;

public class SetNodePropertiesOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter SET_NODE_PROPS_NODE_ID_PARAM =
        new NodeIdFormalParameter("id", SET_NODE_PROPERTIES);

    public SetNodePropertiesOp() {
        super(
            "set_node_properties",
            BasicTypes.VOID_TYPE,
            List.of(SET_NODE_PROPS_NODE_ID_PARAM, PROPERTIES_PARAM)
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