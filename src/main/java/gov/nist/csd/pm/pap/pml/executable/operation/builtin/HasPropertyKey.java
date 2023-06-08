package gov.nist.csd.pm.pap.pml.executable.operation.builtin;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.List;
import java.util.Map;


public class HasPropertyKey extends PMLOperation {

    public HasPropertyKey() {
        super("hasPropertyKey",
                Type.bool(),
                List.of("nodeName", "key"),
                Map.of("nodeName", Type.string(), "key", Type.string())
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        String nodeName = ((Value) operands.get("nodeName")).getStringValue();
        String key = ((Value) operands.get("key")).getStringValue();
        Node node = pap.query().graph().getNode(nodeName);
        boolean hasPropertyKey = node.getProperties().containsKey(key);
        return new BoolValue(hasPropertyKey);
    }
}
