package gov.nist.csd.pm.policy.author.pal.functions;

import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search extends FunctionDefinitionStatement {
    public Search() {
        super(
                name("search"),
                returns(Type.array(Type.string())),
                args(
                        new FormalArgument("type", Type.string()),
                        new FormalArgument("properties", Type.map(Type.string(), Type.string()))
                ),
                (ctx, author) -> {
                    NodeType nodeType = NodeType.toNodeType(ctx.scope().getValue("type").getStringValue());

                    Map<Value, Value> propertiesValue = ctx.scope().getValue("properties").getMapValue();

                    Map<String, String> properties = new HashMap<>();
                    for (Map.Entry<Value, Value> prop : propertiesValue.entrySet()) {
                        properties.put(prop.getKey().getStringValue(), prop.getValue().getStringValue());
                    }

                    List<String> search = author.graph().search(nodeType, properties);

                    Value[] ret = new Value[search.size()];
                    for (int i = 0; i < search.size(); i++) {
                        ret[i] = new Value(search.get(i));
                    }

                    return new Value(ret);
                }
        );
    }
}
