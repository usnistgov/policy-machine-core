package gov.nist.csd.pm.policy.author.pal.function;

import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;

public class Equals extends FunctionDefinitionStatement {

    private static final String VALUE1 = "value1";
    private static final String VALUE2 = "value2";

    public Equals() {
        super(
                name("equals"),
                returns(Type.bool()),
                args(
                        new FormalArgument(VALUE1, Type.any()),
                        new FormalArgument(VALUE2, Type.any())
                ),
                (ctx, author) -> {
                    Value v1 = ctx.getVariable(VALUE1);
                    Value v2 = ctx.getVariable(VALUE2);

                    return new Value(v1.equals(v2));
                }
        );
    }
}
