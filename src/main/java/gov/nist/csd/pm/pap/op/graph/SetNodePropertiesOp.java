package gov.nist.csd.pm.pap.op.graph;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.SET_NODE_PROPERTIES;

public class SetNodePropertiesOp extends GraphOp {

    public SetNodePropertiesOp() {
        super(
                "set_node_properties",
                List.of(NAME_OPERAND, PROPERTIES_OPERAND),
                List.of(NAME_OPERAND)
        );
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().graph().setNodeProperties(
                (String) operands.get(NAME_OPERAND),
                (Map<String, String>) operands.get(PROPERTIES_OPERAND)
        );

        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, (String) operands.get(NAME_OPERAND), SET_NODE_PROPERTIES);
    }
}
