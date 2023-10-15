package gov.nist.csd.pm.policy.pml.function.builtin;


import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.value.BoolValue;


public class Equals extends FunctionDefinitionStatement {

    private static final String VALUE1 = "value1";
    private static final String VALUE2 = "value2";

    public Equals() {
        super(new FunctionDefinitionStatement.Builder("equals")
                      .returns(Type.bool())
                      .args(
                              new FormalArgument(VALUE1, Type.any()),
                              new FormalArgument(VALUE2, Type.any())
                      )
                      .executor((ctx, author) -> {
                          Value v1 = ctx.scope().getValue(VALUE1);
                          Value v2 = ctx.scope().getValue(VALUE2);

                          return new BoolValue(v1.equals(v2));
                      })
                      .build()
        );
    }
}
