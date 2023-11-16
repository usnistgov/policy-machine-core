package gov.nist.csd.pm.policy.pml.function.builtin;

import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.MapValue;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.HashMap;
import java.util.Map;

public class GetNodeProperties extends FunctionDefinitionStatement {

    private static final Type returnType = Type.map(Type.string(), Type.string());


    public GetNodeProperties() {
        super(new FunctionDefinitionStatement.Builder("getNodeProperties")
                      .returns(returnType)
                      .args(
                              new FormalArgument("nodeName", Type.string())
                      )
                      .executor((ctx, author) -> {
                          Node node = author.graph().getNode(ctx.scope().getVariable("nodeName").getStringValue());
                          Map<String, String> properties = node.getProperties();
                          Map<Value, Value> propertiesValues = new HashMap<>();
                          for (Map.Entry<String, String> prop : properties.entrySet()) {
                              propertiesValues.put(new StringValue(prop.getKey()), new StringValue(properties.get(prop.getValue())));
                          }

                          return new MapValue(propertiesValues, Type.string(), Type.string());
                      })
                      .build()
        );
    }

}
