package gov.nist.csd.pm.policy.pml.functions;

import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
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
