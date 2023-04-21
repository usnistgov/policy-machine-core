package gov.nist.csd.pm.policy.pml.functions;

import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

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
