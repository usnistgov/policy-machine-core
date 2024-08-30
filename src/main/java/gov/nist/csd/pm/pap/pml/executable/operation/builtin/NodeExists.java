package gov.nist.csd.pm.pap.pml.executable.operation.builtin;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.List;
import java.util.Map;


public class NodeExists extends PMLOperation {

    private static final String NODE_ARG = "nodeName";

    public NodeExists() {
        super(
                "nodeExists",
                Type.bool(),
                List.of(NODE_ARG),
                Map.of(NODE_ARG, Type.string())
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        return new BoolValue(pap.query().graph().nodeExists(((Value) operands.get(NODE_ARG)).getStringValue()));
    }
}
