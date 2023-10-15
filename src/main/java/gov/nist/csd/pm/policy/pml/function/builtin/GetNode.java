package gov.nist.csd.pm.policy.pml.function.builtin;


import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;


public class GetNode extends FunctionDefinitionStatement {

    private static final String NODE_ARG = "nodeName";

    public GetNode() {
        super(new FunctionDefinitionStatement.Builder("getNode")
                      .returns(Type.map(Type.string(), Type.any()))
                      .args(
                              new FormalArgument(NODE_ARG, Type.string())
                      )
                      .executor((ctx, author) -> {
                          Node node = author.graph().getNode(ctx.scope().getValue(NODE_ARG).getStringValue());

                          return Value.fromObject(node);
                      })
                      .build()
        );
    }
}
