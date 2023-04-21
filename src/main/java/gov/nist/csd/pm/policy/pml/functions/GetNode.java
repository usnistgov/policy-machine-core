package gov.nist.csd.pm.policy.pml.functions;

import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
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
