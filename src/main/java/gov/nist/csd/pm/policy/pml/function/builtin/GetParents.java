package gov.nist.csd.pm.policy.pml.function.builtin;


import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.value.ArrayValue;

import gov.nist.csd.pm.policy.pml.value.StringValue;

import java.util.ArrayList;
import java.util.List;

public class GetParents extends FunctionDefinitionStatement {

    private static final Type returnType = Type.array(Type.string());

    public GetParents() {
        super(new FunctionDefinitionStatement.Builder("getParents")
                      .returns(returnType)
                      .args(
                              new FormalArgument("nodeName", Type.string())
                      )
                      .executor((ctx, author) -> {
                          List<String> parents = author.graph().getParents(ctx.scope().getValue("nodeName").getStringValue());
                          List<Value> parentValues = new ArrayList<>(parents.size());
                          for (int i = 0; i < parents.size(); i++) {
                              parentValues.add(new StringValue(parents.get(i)));
                          }

                          return new ArrayValue(parentValues, returnType);
                      })
                      .build()
        );
    }

}
