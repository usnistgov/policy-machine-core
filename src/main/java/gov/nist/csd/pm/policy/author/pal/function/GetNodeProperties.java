package gov.nist.csd.pm.policy.author.pal.function;

import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;

import java.util.HashMap;
import java.util.Map;

public class GetNodeProperties extends FunctionDefinitionStatement {

    public GetNodeProperties() {
        super(
                name("getNodeProperties"),
                returns(Type.map(Type.string(), Type.string())),
                args(
                        new FormalArgument("nodeName", Type.string())
                ),
                (ctx, author) -> {
                    Node node = author.graph().getNode(ctx.scope().getValue("nodeName").getStringValue());
                    Map<String, String> properties = node.getProperties();
                    Map<Value, Value> propertiesValues = new HashMap<>();
                    for (String k : properties.keySet()) {
                        propertiesValues.put(new Value(k), new Value(properties.get(k)));
                    }

                    return new Value(propertiesValues);
                }
        );
    }

}
