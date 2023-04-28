package gov.nist.csd.pm.policy.pml.functions;

import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;

public class Length extends FunctionDefinitionStatement {
    public Length() {
        super(
                "length",
                returns(Type.number()),
                args(
                        new FormalArgument("x", Type.any())
                ),
                (ctx, policy) -> {
                    Value x = ctx.scope().getValue("x");
                    if (x.isMap()) {
                        return new Value(x.getMapValue().size());
                    } else if (x.isArray()) {
                        return new Value(x.getArrayValue().size());
                    } else if (x.isString()) {
                        return new Value(x.getStringValue().length());
                    }

                    throw new PMException("cannot get length of type " + x.getType());
                }
        );
    }
}
