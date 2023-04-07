package gov.nist.csd.pm.policy.author.pal.functions;

import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;

import java.util.ArrayList;
import java.util.List;

public class GetParents extends FunctionDefinitionStatement {

    public GetParents() {
        super(
                name("getParents"),
                returns(Type.array(Type.string())),
                args(
                        new FormalArgument("nodeName", Type.string())
                ),
                (ctx, author) -> {
                    List<String> parents = author.getParents(ctx.scope().getValue("nodeName").getStringValue());
                    List<Value> parentValues = new ArrayList<>(parents.size());
                    for (int i = 0; i < parents.size(); i++) {
                        parentValues.add(new Value(parents.get(i)));
                    }

                    return new Value(parentValues);
                }
        );
    }

}
