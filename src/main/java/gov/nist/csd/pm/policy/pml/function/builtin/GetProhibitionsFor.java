package gov.nist.csd.pm.policy.pml.function.builtin;


import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.ArrayValue;
import gov.nist.csd.pm.policy.pml.value.ProhibitionValue;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.ArrayList;
import java.util.List;

public class GetProhibitionsFor extends FunctionDefinitionStatement {

    private static final Type returnType = Type.array(Type.map(Type.string(), Type.any()));


    public GetProhibitionsFor() {
        super(new FunctionDefinitionStatement.Builder("getProhibitionsFor")
                      .returns(returnType)
                      .args(
                              new FormalArgument("subject", Type.string())
                      )
                      .executor((ctx, author) -> {
                                    String subject = ctx.scope().getValue("subject").getStringValue();
                                    List<Prohibition> prohibitions = author.prohibitions().getWithSubject(subject);
                                    List<Value> prohibitionValues = new ArrayList<>(prohibitions.size());
                                    for (Prohibition prohibition : prohibitions) {
                                        prohibitionValues.add(new ProhibitionValue(prohibition).getValue());
                                    }

                                    return new ArrayValue(prohibitionValues, returnType);
                                }
                      )
                      .build()
        );
    }

}
