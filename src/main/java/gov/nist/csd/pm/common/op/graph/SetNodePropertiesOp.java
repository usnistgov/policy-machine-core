package gov.nist.csd.pm.common.op.graph;


import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.SET_NODE_PROPERTIES;

public class SetNodePropertiesOp extends GraphOp {

    public SetNodePropertiesOp() {
        super(
                "set_node_properties",
                List.of(ID_OPERAND, PROPERTIES_OPERAND),
                List.of(ID_OPERAND)
        );
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().graph().setNodeProperties(
                (long) operands.get(ID_OPERAND),
                (Map<String, String>) operands.get(PROPERTIES_OPERAND)
        );

        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, (long) operands.get(ID_OPERAND), SET_NODE_PROPERTIES);
    }

    @Override
    public EventContext toEventContext(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {
        Map<String, Object> operandsWithNames = new HashMap<>();

        long id = (long) operands.get(NAME_OPERAND);
        operandsWithNames.put(NAME_OPERAND, pap.query().graph().getNodeById(id).getName());

        return new EventContext(
                userCtx.getUser(),
                userCtx.getProcess(),
                this,
                operandsWithNames
        );
    }
}
