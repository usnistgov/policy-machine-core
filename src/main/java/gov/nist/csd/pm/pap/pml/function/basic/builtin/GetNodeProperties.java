package gov.nist.csd.pm.pap.pml.function.basic.builtin;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.MapValue;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetNodeProperties extends PMLBasicFunction {

    private static final Type returnType = Type.map(Type.string(), Type.string());


    public GetNodeProperties() {
        super(
                "getNodeProperties",
                returnType,
                List.of(NODE_NAME_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
        Node node = pap.query().graph().getNodeByName(actualArgs.get(NODE_NAME_ARG).getStringValue());
        Map<String, String> properties = node.getProperties();
        Map<Value, Value> propertiesValues = new HashMap<>();
        for (Map.Entry<String, String> prop : properties.entrySet()) {
            propertiesValues.put(new StringValue(prop.getKey()), new StringValue(properties.get(prop.getValue())));
        }

        return new MapValue(propertiesValues, Type.string(), Type.string());
    }
}
