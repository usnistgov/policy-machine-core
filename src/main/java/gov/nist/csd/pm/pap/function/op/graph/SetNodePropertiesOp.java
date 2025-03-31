package gov.nist.csd.pm.pap.function.op.graph;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Properties;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.Args;
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
    
    public Args actualArgs(long nodeId, Properties properties) {
        Args args = new Args();
        args.put(NODE_ARG, nodeId);
        args.put(PROPERTIES_ARG, properties);
        return args;
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().graph().setNodeProperties(
		        args.get(NODE_ARG),
		        args.get(PROPERTIES_ARG)
        );

        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Args args) throws PMException {
        privilegeChecker.check(userCtx, args.get(NODE_ARG), SET_NODE_PROPERTIES);
    }
}
