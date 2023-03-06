package gov.nist.csd.pm.policy.author.pal.functions;

import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;

public class NodeExists extends FunctionDefinitionStatement {

    private static final String NODE_ARG = "nodeName";

    public NodeExists() {
        super(
                name("nodeExists"),
                returns(Type.bool()),
                args(
                        new FormalArgument(NODE_ARG, Type.string())
                ),
                (ctx, author) -> new Value(author.nodeExists(ctx.scope().getValue(NODE_ARG).getStringValue()))
        );
    }
}
