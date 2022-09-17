package gov.nist.csd.pm.policy.author.pal.function;

import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;

import java.util.List;

public class GetChildren extends FunctionDefinitionStatement {

    public GetChildren() {
        super(
                name("getChildren"),
                returns(Type.array(Type.string())),
                args(
                        new FormalArgument("nodeName", Type.string())
                ),
                (ctx, author) -> {
                    List<String> children = author.graph().getChildren(ctx.scope().getValue("nodeName").getStringValue());
                    Value[] childValues = new Value[children.size()];
                    for (int i = 0; i < children.size(); i++) {
                        childValues[i] = new Value(children.get(i));
                    }

                    return new Value(childValues);
                }
        );
    }

}
