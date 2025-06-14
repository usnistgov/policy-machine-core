package gov.nist.csd.pm.core.pap.function.op.graph;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.op.graph.SetNodePropertiesOp.SetNodePropertiesOpArgs;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.SET_NODE_PROPERTIES;

public class SetNodePropertiesOp extends GraphOp<Void, SetNodePropertiesOpArgs> {

    public SetNodePropertiesOp() {
        super(
                "set_node_properties",
                List.of(NODE_PARAM, PROPERTIES_PARAM)
        );
    }

    @Override
    protected SetNodePropertiesOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        return new SetNodePropertiesOpArgs(
            prepareArg(NODE_PARAM, argsMap),
            prepareArg(PROPERTIES_PARAM, argsMap)
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, SetNodePropertiesOpArgs args) throws PMException {
        pap.privilegeChecker().check(userCtx, args.get(NODE_PARAM), SET_NODE_PROPERTIES);
    }

    @Override
    public Void execute(PAP pap, SetNodePropertiesOpArgs args) throws PMException {
        pap.modify().graph().setNodeProperties(
            args.getNodeId(),
            args.getProperties()
        );

        return null;
    }

    public static class SetNodePropertiesOpArgs extends Args {
        private long nodeId;
        private Map<String, String> properties;
        public SetNodePropertiesOpArgs(long nodeId, Map<String, String> properties) {
            super(Map.of(
                NODE_PARAM, nodeId,
                PROPERTIES_PARAM, properties
            ));

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
