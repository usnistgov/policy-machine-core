package gov.nist.csd.pm.policy.pml.functions;

import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.ArrayList;
import java.util.List;

public class GetAssociationsWithTarget extends FunctionDefinitionStatement {

    public GetAssociationsWithTarget() {
        super(
                name("getAssociationsWithTarget"),
                returns(Type.array(Type.map(Type.string(), Type.any()))),
                args(
                        new FormalArgument("target", Type.string())
                ),
                (ctx, author) -> {
                    Value target = ctx.scope().getValue("target");
                    List<Association> associations = author.getAssociationsWithTarget(target.getStringValue());
                    List<Value> associationValues = new ArrayList<>(associations.size());
                    for (int i = 0; i < associations.size(); i++)  {
                        Association association = associations.get(i);
                        associationValues.add(Value.objectToValue(association));
                    }
                    return new Value(associationValues);
                }
        );
    }

}
