package gov.nist.csd.pm.policy.pml.functions;

import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.List;

public class AppendAll extends FunctionDefinitionStatement {
    public AppendAll() {
        super(
                "appendAll",
                Type.array(Type.any()),
                args(
                        new FormalArgument("dst", Type.array(Type.any())),
                        new FormalArgument("src", Type.array(Type.any()))
                ),
                (ctx, policy) -> {
                    List<Value> dstValueArr = ctx.scope().getValue("dst").getArrayValue();
                    List<Value> srcValueArr = ctx.scope().getValue("src").getArrayValue();

                    dstValueArr.addAll(srcValueArr);

                    return new Value(dstValueArr);
                }
        );
    }
}
