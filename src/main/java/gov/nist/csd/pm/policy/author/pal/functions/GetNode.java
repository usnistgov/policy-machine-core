package gov.nist.csd.pm.policy.author.pal.functions;

import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;

import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;

public class GetNode extends FunctionDefinitionStatement {

    private static final String NODE_ARG = "nodeName";

    public GetNode() {
        super(
                name("getNode"),
                returns(Type.map(Type.string(), Type.any())),
                args(
                        new FormalArgument(NODE_ARG, Type.string())
                ),
                (ctx, author) -> {
                    Node node = author.getNode(ctx.scope().getValue(NODE_ARG).getStringValue());
                    return Value.objectToValue(node);
                }
        );
    }
}
