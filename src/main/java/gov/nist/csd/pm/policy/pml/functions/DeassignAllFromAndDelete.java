package gov.nist.csd.pm.policy.pml.functions;

import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

public class DeassignAllFromAndDelete extends FunctionDefinitionStatement {
    public DeassignAllFromAndDelete() {
        super(
                "deassignAllFromAndDelete",
                Type.voidType(),
                args(
                        new FormalArgument("target", Type.string())
                ),
                (ctx, policy) -> {
                    String target = ctx.scope().getValue("target").getStringValue();

                    policy.deassignAllFromAndDelete(target);

                    return new Value();
                }
        );
    }
}
