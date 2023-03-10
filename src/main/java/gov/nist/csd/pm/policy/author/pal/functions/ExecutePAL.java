package gov.nist.csd.pm.policy.author.pal.functions;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.author.pal.PALExecutor;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

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
