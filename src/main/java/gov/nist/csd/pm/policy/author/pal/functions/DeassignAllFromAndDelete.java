package gov.nist.csd.pm.policy.author.pal.functions;

import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;

import java.util.ArrayList;
import java.util.List;

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
