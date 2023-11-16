package gov.nist.csd.pm.policy.pml.function.builtin;


import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.BoolValue;


public class HasPropertyValue extends FunctionDefinitionStatement {

    public HasPropertyValue() {
        super(new FunctionDefinitionStatement.Builder("hasPropertyValue")
                      .returns(Type.bool())
                      .args(
                              new FormalArgument("nodeName", Type.string()),
                              new FormalArgument("key", Type.string()),
                              new FormalArgument("value", Type.string())
                      )
                      .executor((ctx, author) -> {
                          String nodeName = ctx.scope().getVariable("nodeName").getStringValue();
                          String key = ctx.scope().getVariable("key").getStringValue();
                          String value = ctx.scope().getVariable("value").getStringValue();
                          Node node = author.graph().getNode(nodeName);
                          boolean has = node.getProperties().containsKey(key);
                          if (!has) {
                              return new BoolValue(false);
                          }

                          has = node.getProperties().get(key).equals(value);
                          return new BoolValue(has);
                      })
                      .build()
        );
    }

}
