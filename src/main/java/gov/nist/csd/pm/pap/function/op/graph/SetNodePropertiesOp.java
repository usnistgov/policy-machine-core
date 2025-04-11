package gov.nist.csd.pm.pap.function.op.graph;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.graph.SetNodePropertiesOp.SetNodeProeprtiesOpArgs;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.SET_NODE_PROPERTIES;

public class SetNodePropertiesOp extends GraphOp<Void, SetNodeProeprtiesOpArgs> {

    public SetNodePropertiesOp() {
        super(
                "set_node_properties",
                List.of(NODE_ARG, PROPERTIES_ARG)
        );
    }

    @Override
    public Void execute(PAP pap, SetNodeProeprtiesOpArgs args) throws PMException {
        pap.modify().graph().setNodeProperties(
		        args.get(NODE_ARG),
		        args.get(PROPERTIES_ARG)
        );

        return null;
    }

    @Override
    protected SetNodeProeprtiesOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        return new SetNodeProeprtiesOpArgs(
            prepareArg(NODE_ARG, argsMap),
            prepareArg(PROPERTIES_ARG, argsMap)
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, SetNodeProeprtiesOpArgs args) throws PMException {
        privilegeChecker.check(userCtx, args.get(NODE_ARG), SET_NODE_PROPERTIES);
    }

    public static class SetNodeProeprtiesOpArgs extends Args {
        private long nodeId;
        private Map<String, String> properties;
        public SetNodeProeprtiesOpArgs(long nodeId, Map<String, String> properties) {
            this.nodeId = nodeId;
            this.properties = properties;
        }

        public long getNodeId() {
            return nodeId;
        }

        public Map<String, String> getProperties() {
            return properties;
        }
    }
}
