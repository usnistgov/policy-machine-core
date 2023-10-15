package gov.nist.csd.pm.policy.pml.function.builtin;


import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.pml.value.BoolValue;


public class HasPropertyKey extends FunctionDefinitionStatement {

    public HasPropertyKey() {
        super(new FunctionDefinitionStatement.Builder("hasPropertyKey")
                      .returns(Type.bool())
                      .args(
                              new FormalArgument("nodeName", Type.string()),
                              new FormalArgument("key", Type.string())
                      )
                      .executor((ctx, author) -> {
                          String nodeName = ctx.scope().getValue("nodeName").getStringValue();
                          String key = ctx.scope().getValue("key").getStringValue();
                          Node node = author.graph().getNode(nodeName);
                          boolean hasPropertyKey = node.getProperties().containsKey(key);
                          return new BoolValue(hasPropertyKey);
                      })
                      .build()
        );
    }

}
