package gov.nist.csd.pm.policy.author.pal.functions;

import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.ArrayList;
import java.util.List;

public class GetProhibitionsFor extends FunctionDefinitionStatement {

    public GetProhibitionsFor() {
        super(
                name("getProhibitionsFor"),
                returns(Type.array(Type.map(Type.string(), Type.any()))),
                args(
                        new FormalArgument("subject", Type.string())
                ),
                (ctx, author) -> {
                    String subject = ctx.scope().getValue("subject").getStringValue();
                    List<Prohibition> prohibitions = author.getProhibitionsWithSubject(subject);
                    List<Value> prohibitionValues = new ArrayList<>(prohibitions.size());
                    for (Prohibition prohibition : prohibitions) {
                        prohibitionValues.add(new Value(Value.objectToValue(prohibition)));
                    }

                    return new Value(prohibitionValues);
                }
        );
    }

}
