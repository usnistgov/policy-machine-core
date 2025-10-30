package gov.nist.csd.pm.core.pap.function.op.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.SET_NODE_PROPERTIES;

public class SetNodePropertiesOp extends GraphOp<Void> {

    public SetNodePropertiesOp() {
        super(
                "set_node_properties",
                List.of(NODE_PARAM, PROPERTIES_PARAM)
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        pap.privilegeChecker().check(userCtx, args.get(NODE_PARAM), SET_NODE_PROPERTIES);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().graph().setNodeProperties(args.get(NODE_PARAM), args.get(PROPERTIES_PARAM));

        return null;
    }
}
