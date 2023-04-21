package gov.nist.csd.pm.policy.pml.functions;

import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.List;

public class Append extends FunctionDefinitionStatement {
    public Append() {
        super(
                "append",
                Type.array(Type.any()),
                args(
                        new FormalArgument("dst", Type.array(Type.any())),
                        new FormalArgument("src", Type.any())
                ),
                (ctx, policy) -> {
                    List<Value> valueArr = ctx.scope().getValue("dst").getArrayValue();
                    Value srcValue = ctx.scope().getValue("src").getValue();

                    valueArr.add(srcValue);

                    return new Value(valueArr);
                }
        );
    }
}
