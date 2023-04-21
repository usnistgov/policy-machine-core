package gov.nist.csd.pm.policy.pml.functions;

import gov.nist.csd.pm.policy.pml.PALExecutor;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.List;

public class ExecutePAL extends FunctionDefinitionStatement {

    public ExecutePAL() {
        super(
                "executePAL",
                Type.voidType(),
                List.of(
                        new FormalArgument("pal", Type.string())
                ),
                (ctx, policy) -> {
                    PALExecutor.compileAndExecutePAL(policy, ctx.author(), ctx.scope().getValue("pal").getStringValue());
                    return new Value();
                }
        );
    }
}
