package gov.nist.csd.pm.policy.author.pal.functions;

import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;

public class NumToStr extends FunctionDefinitionStatement {

    private static final String NUM_ARG = "num";

    public NumToStr() {
        super(
                name("numToStr"),
                returns(Type.string()),
                args(
                        new FormalArgument(NUM_ARG, Type.number())
                ),
                (ctx, author) -> new Value(String.valueOf(ctx.scope().getValue(NUM_ARG)))
        );
    }
}
