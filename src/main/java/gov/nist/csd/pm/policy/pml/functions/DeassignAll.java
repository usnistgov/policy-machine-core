package gov.nist.csd.pm.policy.pml.functions;

import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.ArrayList;
import java.util.List;

public class DeassignAll extends FunctionDefinitionStatement {
    public DeassignAll() {
        super(
                "deassignAll",
                Type.voidType(),
                args(
                        new FormalArgument("children", Type.array(Type.string())),
                        new FormalArgument("target", Type.string())
                ),
                (ctx, policy) -> {
                    String target = ctx.scope().getValue("target").getStringValue();
                    List<Value> childrenValues = ctx.scope().getValue("children").getArrayValue();
                    List<String> children = new ArrayList<>();
                    for (Value value : childrenValues) {
                        children.add(value.getStringValue());
                    }

                    policy.graph().deassignAll(children, target);

                    return new Value();
                }
        );
    }
}
