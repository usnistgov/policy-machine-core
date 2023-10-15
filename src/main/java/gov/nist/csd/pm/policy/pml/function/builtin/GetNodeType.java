package gov.nist.csd.pm.policy.pml.function.builtin;


import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;

import gov.nist.csd.pm.policy.pml.value.StringValue;

public class GetNodeType extends FunctionDefinitionStatement {

    public GetNodeType() {
        super(new FunctionDefinitionStatement.Builder("getNodeType")
                      .returns(Type.string())
                      .args(
                              new FormalArgument("nodeName", Type.string())
                      )
                      .executor((ctx, author) -> {
                          Node node = author.graph().getNode(ctx.scope().getValue("nodeName").getStringValue());
                          return new StringValue(node.getType().toString());
                      })
                      .build()
        );
    }

}

