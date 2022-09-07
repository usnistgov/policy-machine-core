package gov.nist.csd.pm.policy.author.pal.function;

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
                    String nodeName = ctx.getVariable("nodeName").getStringValue();
                    String key = ctx.getVariable("key").getStringValue();
                    String value = ctx.getVariable("value").getStringValue();
                    Node node = author.graph().getNode(nodeName);
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
