package gov.nist.csd.pm.pap.pml.executable.operation.builtin;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

public class GetNodeType extends PMLOperation {

    public GetNodeType() {
        super(
                "getNodeType",
                Type.string(),
                List.of("nodeName"),
                Map.of("nodeName", Type.string())
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        Node node = pap.query().graph().getNode(((Value) operands.get("nodeName")).getStringValue());
        return new StringValue(node.getType().toString());
    }
}

