package gov.nist.ngac.pm.core.pap.operation.graph;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.AdminOperation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.ngac.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredPrivilegeOnParameter;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;

/**
 * Admin operation "set_node_properties". Replaces a node's properties.
 */
public class SetNodePropertiesOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter SET_NODE_PROPS_NODE_ID_PARAM =
        new NodeIdFormalParameter("id");

    public SetNodePropertiesOp() {
        super(
            "set_node_properties",
            BasicTypes.VOID_TYPE,
            List.of(SET_NODE_PROPS_NODE_ID_PARAM, PROPERTIES_PARAM),
            new RequiredCapability(
                new RequiredPrivilegeOnParameter(SET_NODE_PROPS_NODE_ID_PARAM, AdminAccessRight.ADMIN_GRAPH_NODE_UPDATE)
            )
        );
    }

    @Override
    public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
        pap.modify().graph().setNodeProperties(
            args.get(SET_NODE_PROPS_NODE_ID_PARAM),
            args.get(PROPERTIES_PARAM)
        );

        return null;
    }
}