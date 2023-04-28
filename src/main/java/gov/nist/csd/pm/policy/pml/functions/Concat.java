package gov.nist.csd.pm.policy.pml.functions;

import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;

import java.util.List;

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
                    List<Value> arr = ctx.scope().getValue(ARR_ARG).getArrayValue();
                    StringBuilder s = new StringBuilder();
                    for (Value v : arr) {
                        s.append(v.getStringValue());
                    }
                    return new Value(s.toString());
                }
        );
    }
}
