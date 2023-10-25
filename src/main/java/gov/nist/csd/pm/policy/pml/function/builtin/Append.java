package gov.nist.csd.pm.policy.pml.function.builtin;


import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.ArrayValue;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.List;

public class Append extends FunctionDefinitionStatement {
    public Append() {
        super(new FunctionDefinitionStatement.Builder("append")
                      .returns(Type.array(Type.any()))
                      .args(
                              new FormalArgument("dst", Type.array(Type.any())),
                              new FormalArgument("src", Type.any())
                      )
                      .executor((ctx, author) -> {
                          List<Value> valueArr = ctx.scope().getValue("dst").getArrayValue();
                          Value srcValue = ctx.scope().getValue("src");

                          valueArr.add(srcValue);

                          return new ArrayValue(valueArr, Type.array(Type.any()));
                      })
                      .build()
        );
    }
}
