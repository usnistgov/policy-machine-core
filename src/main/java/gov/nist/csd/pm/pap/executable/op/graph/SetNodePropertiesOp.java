package gov.nist.csd.pm.pap.executable.op.graph;


import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.SET_NODE_PROPERTIES;

public class SetNodePropertiesOp extends GraphOp<Void> {

    public SetNodePropertiesOp() {
        super(
                "set_node_properties",
                List.of(NODE_OPERAND, PROPERTIES_OPERAND),
                List.of(NODE_OPERAND)
        );
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().graph().setNodeProperties(
                (long) operands.get(NODE_OPERAND),
                (Map<String, String>) operands.get(PROPERTIES_OPERAND)
        );

        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, (long) operands.get(NODE_OPERAND), SET_NODE_PROPERTIES);
    }

    public static class EventCtx extends EventContext {

        public EventCtx(String user, String process, String nodeName) {
            super(user, process, "set_node_properties", Map.of(
                    NODE_OPERAND, nodeName
            ));
        }
    }
}
