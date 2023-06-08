package gov.nist.csd.pm.pap.pml.executable.operation.builtin;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.MapValue;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetNodeProperties extends PMLOperation {

    private static final Type returnType = Type.map(Type.string(), Type.string());


    public GetNodeProperties() {
        super(
                "getNodeProperties",
                returnType,
                List.of("nodeName"),
                Map.of("nodeName", Type.string())
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        Node node = pap.query().graph().getNode(((Value) operands.get("nodeName")).getStringValue());
        Map<String, String> properties = node.getProperties();
        Map<Value, Value> propertiesValues = new HashMap<>();
        for (Map.Entry<String, String> prop : properties.entrySet()) {
            propertiesValues.put(new StringValue(prop.getKey()), new StringValue(properties.get(prop.getValue())));
        }

        return new MapValue(propertiesValues, Type.string(), Type.string());
    }
}
