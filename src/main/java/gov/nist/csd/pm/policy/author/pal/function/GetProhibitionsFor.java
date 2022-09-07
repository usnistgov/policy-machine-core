package gov.nist.csd.pm.policy.author.pal.function;

import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

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
                    String subject = ctx.getVariable("subject").getStringValue();
                    List<Prohibition> prohibitions = author.prohibitions().withSubject(subject);
                    Value[] prohibitionValues = new Value[prohibitions.size()];
                    for (int i = 0; i < prohibitions.size(); i++) {
                        prohibitionValues[i] = new Value(Value.objectToValue(prohibitions.get(i)));
                    }

                    return new Value(prohibitionValues);
                }
        );
    }

}
