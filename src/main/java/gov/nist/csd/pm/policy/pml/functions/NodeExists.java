package gov.nist.csd.pm.policy.pml.functions;

import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;

public class NodeExists extends FunctionDefinitionStatement {

    private static final String NODE_ARG = "nodeName";

    public NodeExists() {
        super(
                name("nodeExists"),
                returns(Type.bool()),
                args(
                        new FormalArgument(NODE_ARG, Type.string())
                ),
                (ctx, author) -> new Value(author.graph().nodeExists(ctx.scope().getValue(NODE_ARG).getStringValue()))
        );
    }
}
