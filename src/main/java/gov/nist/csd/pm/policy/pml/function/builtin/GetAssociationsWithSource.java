package gov.nist.csd.pm.policy.pml.function.builtin;

import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.ArrayValue;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.ArrayList;
import java.util.List;

public class GetAssociationsWithSource extends FunctionDefinitionStatement {

    private static final Type returnType = Type.array(Type.map(Type.string(), Type.any()));

    public GetAssociationsWithSource() {
        super(new FunctionDefinitionStatement.Builder("getAssociationsWithSource")
                      .returns(returnType)
                      .args(
                              new FormalArgument("source", Type.string())
                      )
                      .executor((ctx, author) -> {
                          Value source = ctx.scope().getValue("source");
                          List<Association> associations = author.graph().getAssociationsWithSource(source.getStringValue());
                          List<Value> associationValues = new ArrayList<>(associations.size());
                          for (Association association : associations) {
                              associationValues.add(Value.fromObject(association));
                          }

                          return new ArrayValue(associationValues, returnType);
                      })
                      .build()
        );
    }

}
