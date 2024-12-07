package gov.nist.csd.pm.pap.pml.executable.operation.builtin;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;


public class HasPropertyValue extends PMLOperation {

    public HasPropertyValue() {
        super(
                "hasPropertyValue",
                Type.bool(),
                List.of("nodeName", "key", "value"),
                Map.of("nodeName", Type.string(), "key", Type.string(), "value", Type.string())
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        String nodeName = ((Value) operands.get("nodeName")).getStringValue();
        String key = ((Value) operands.get("key")).getStringValue();
        String value = ((Value) operands.get("value")).getStringValue();
        Node node = pap.query().graph().getNode(nodeName);
        boolean has = node.getProperties().containsKey(key);
        if (!has) {
            return new BoolValue(false);
        }

        has = node.getProperties().get(key).equals(value);
        return new BoolValue(has);
    }
}
