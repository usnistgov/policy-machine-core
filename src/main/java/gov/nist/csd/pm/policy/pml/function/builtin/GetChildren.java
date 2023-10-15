package gov.nist.csd.pm.policy.pml.function.builtin;


import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.value.ArrayValue;

import gov.nist.csd.pm.policy.pml.value.StringValue;

import java.util.ArrayList;
import java.util.List;

public class GetChildren extends FunctionDefinitionStatement {

    private static final Type returnType = Type.array(Type.string());

    public GetChildren() {
        super(new FunctionDefinitionStatement.Builder("getChildren")
                      .returns(returnType)
                      .args(
                              new FormalArgument("nodeName", Type.string())
                      )
                      .executor((ctx, author) -> {
                          List<String> children = author.graph().getChildren(ctx.scope().getValue("nodeName").getStringValue());
                          List<Value> childValues = new ArrayList<>(children.size());
                          for (int i = 0; i < children.size(); i++) {
                              childValues.add(new StringValue(children.get(i)));
                          }

                          return new ArrayValue(childValues, returnType);
                      })
                      .build()
        );
    }

}
