package gov.nist.csd.pm.policy.pml.function.builtin;


import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.List;

public class Concat extends FunctionDefinitionStatement {

    private static final String ARR_ARG = "arr";

    public Concat() {
        super(new FunctionDefinitionStatement.Builder("concat")
                      .returns(Type.string())
                      .args(
                              new FormalArgument(ARR_ARG, Type.array(Type.string()))
                      )
                      .executor((ctx, author) -> {
                          List<Value> arr = ctx.scope().getValue(ARR_ARG).getArrayValue();
                          StringBuilder s = new StringBuilder();
                          for (Value v : arr) {
                              s.append(v.getStringValue());
                          }

                          return new StringValue(s.toString());
                      })
                      .build()
        );
    }
}
