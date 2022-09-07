package gov.nist.csd.pm.policy.author.pal.function;

import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.List;

public class GetAssociationsWithSource extends FunctionDefinitionStatement {

    public GetAssociationsWithSource() {
        super(
                name("getAssociationsWithSource"),
                returns(Type.array(Type.map(Type.string(), Type.any()))),
                args(
                        new FormalArgument("source", Type.string())
                ),
                (ctx, author) -> {
                    Value source = ctx.getVariable("source");
                    List<Association> associations = author.graph().getAssociationsWithSource(source.getStringValue());
                    Value[] associationValues = new Value[associations.size()];
                    for (int i = 0; i < associations.size(); i++)  {
                        Association association = associations.get(i);
                        associationValues[i] = Value.objectToValue(association);
                    }
                    return new Value(associationValues);
                }
        );
    }

}
