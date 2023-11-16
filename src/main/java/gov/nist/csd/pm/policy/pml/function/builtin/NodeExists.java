package gov.nist.csd.pm.policy.pml.function.builtin;


import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.BoolValue;


public class NodeExists extends FunctionDefinitionStatement {

    private static final String NODE_ARG = "nodeName";

    public NodeExists() {
        super(new FunctionDefinitionStatement.Builder("nodeExists")
                      .returns(Type.bool())
                      .args(
                              new FormalArgument(NODE_ARG, Type.string())
                      )
                      .executor((ctx, author) -> {
                          return new BoolValue(author.graph().nodeExists(ctx.scope().getVariable(NODE_ARG).getStringValue()));
                      })
                      .build()
        );
    }
}
