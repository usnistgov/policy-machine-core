package gov.nist.csd.pm.core.pap.function.op.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeFormalParameter;

import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.SET_NODE_PROPERTIES;

public class SetNodePropertiesOp extends AdminOperation<Void> {

    public static final NodeFormalParameter SET_NODE_PROPS_NODE_PARAM =
        new NodeFormalParameter("node", SET_NODE_PROPERTIES);

    public SetNodePropertiesOp() {
        super(
                "set_node_properties",
                List.of(SET_NODE_PROPS_NODE_PARAM, PROPERTIES_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().graph().setNodeProperties(
            args.get(SET_NODE_PROPS_NODE_PARAM).getId(pap),
            args.get(PROPERTIES_PARAM)
        );

        return null;
    }
}
