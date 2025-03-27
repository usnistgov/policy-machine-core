package gov.nist.csd.pm.pap.executable.op.graph;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Properties;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.SET_NODE_PROPERTIES;

public class SetNodePropertiesOp extends GraphOp<Void> {

    public SetNodePropertiesOp() {
        super(
                "set_node_properties",
                List.of(NODE_ARG, PROPERTIES_ARG)
        );
    }
    
    public ActualArgs actualArgs(long nodeId, Properties properties) {
        ActualArgs actualArgs = new ActualArgs();
        actualArgs.put(NODE_ARG, nodeId);
        actualArgs.put(PROPERTIES_ARG, properties);
        return actualArgs;
    }

    @Override
    public Void execute(PAP pap, ActualArgs actualArgs) throws PMException {
        pap.modify().graph().setNodeProperties(
		        actualArgs.get(NODE_ARG),
		        actualArgs.get(PROPERTIES_ARG)
        );

        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs args) throws PMException {
        privilegeChecker.check(userCtx, args.get(NODE_ARG), SET_NODE_PROPERTIES);
    }
}
