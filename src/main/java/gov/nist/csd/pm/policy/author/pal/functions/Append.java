package gov.nist.csd.pm.policy.author.pal.functions;

import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;

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
