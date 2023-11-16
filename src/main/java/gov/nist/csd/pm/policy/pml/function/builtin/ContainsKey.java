package gov.nist.csd.pm.policy.pml.function.builtin;


import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.BoolValue;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.Map;

public class ContainsKey extends FunctionDefinitionStatement {

    public ContainsKey() {
        super(new FunctionDefinitionStatement.Builder("containsKey")
                      .returns(Type.bool())
                      .args(
                              new FormalArgument("map", Type.map(Type.any(), Type.any())),
                              new FormalArgument("key", Type.any())
                      )
                      .executor((ctx, author) -> {
                          Map<Value, Value> valueMap = ctx.scope().getVariable("map").getMapValue();
                          Value element = ctx.scope().getVariable("key");
                          boolean contains = valueMap.containsKey(element);
                          return new BoolValue(contains);
                      })
                      .build()
        );
    }

}
