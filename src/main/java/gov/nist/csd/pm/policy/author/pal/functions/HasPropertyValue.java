package gov.nist.csd.pm.policy.author.pal.functions;

import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;

public class HasPropertyValue extends FunctionDefinitionStatement {

    public HasPropertyValue() {
        super(
                name("hasPropertyValue"),
                returns(Type.bool()),
                args(
                        new FormalArgument("nodeName", Type.string()),
                        new FormalArgument("key", Type.string()),
                        new FormalArgument("value", Type.string())
                ),
                (ctx, author) -> {
                    String nodeName = ctx.scope().getValue("nodeName").getStringValue();
                    String key = ctx.scope().getValue("key").getStringValue();
                    String value = ctx.scope().getValue("value").getStringValue();
                    Node node = author.getNode(nodeName);
                    boolean has = node.getProperties().containsKey(key);
                    if (!has) {
                        return new Value(false);
                    }

                    has = node.getProperties().get(key).equals(value);
                    return new Value(has);
                }
        );
    }

}
