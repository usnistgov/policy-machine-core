package gov.nist.csd.pm.policy.pml.functions;

import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;

import java.util.List;

public class Contains extends FunctionDefinitionStatement {

    public Contains() {
        super(
                name("contains"),
                returns(Type.bool()),
                args(
                        new FormalArgument("arr", Type.array(Type.any())),
                        new FormalArgument("element", Type.any())
                ),
                (ctx, author) -> {
                    List<Value> valueArr = ctx.scope().getValue("arr").getArrayValue();
                    Value element = ctx.scope().getValue("element");
                    boolean contains = valueArr.contains(element);
                    return new Value(contains);
                }
        );
    }

}

