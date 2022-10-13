package gov.nist.csd.pm.policy.author.pal.functions;

import gov.nist.csd.pm.pap.naming.Naming;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;

public class BaseOA extends FunctionDefinitionStatement {
    public BaseOA() {
        super(
                new Builder("baseOA")
                .args(
                        new FormalArgument("policyClass", Type.string())
                )
                .returns(Type.string())
                .executor((ctx, policy) -> {
                    Value value = ctx.scope().getValue("policyClass");
                    String policyClass = value.getStringValue();
                    return new Value(Naming.baseObjectAttribute(policyClass));
                })
                .build()
        );
    }
}
