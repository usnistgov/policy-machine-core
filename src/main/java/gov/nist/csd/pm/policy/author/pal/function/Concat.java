package gov.nist.csd.pm.policy.author.pal.function;

import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;

public class Concat extends FunctionDefinitionStatement {

    private static final String ARR_ARG = "arr";

    public Concat() {
        super(
                name("concat"),
                returns(Type.string()),
                args(
                        new FormalArgument(ARR_ARG, Type.array(Type.string()))
                ),
                (ctx, author) -> {
                    Value[] arr = ctx.getVariable(ARR_ARG).getArrayValue();
                    StringBuilder s = new StringBuilder();
                    for (Value v : arr) {
                        s.append(v.getStringValue());
                    }
                    return new Value(s.toString());
                }
        );
    }
}
