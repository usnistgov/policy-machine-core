package gov.nist.csd.pm.policy.pml.functions;

import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;

public class GetNodeType extends FunctionDefinitionStatement {

    public GetNodeType() {
        super(
                name("getNodeType"),
                returns(Type.string()),
                args(
                        new FormalArgument("nodeName", Type.string())
                ),
                (ctx, author) -> {
                    Node node = author.getNode(ctx.scope().getValue("nodeName").getStringValue());
                    return new Value(node.getType().toString());
                }
        );
    }

}

