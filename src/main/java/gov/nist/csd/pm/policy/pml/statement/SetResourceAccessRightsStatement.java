package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;

public class SetResourceAccessRightsStatement extends PMLStatement{

    private final Expression arExpr;

    public SetResourceAccessRightsStatement(Expression arExprList) {
        this.arExpr = arExprList;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        Value arValue = arExpr.execute(ctx, policy);
        AccessRightSet accessRightSet = new AccessRightSet();
        for (Value v : arValue.getArrayValue()) {
            accessRightSet.add(v.getStringValue());
        }

        policy.graph().setResourceAccessRights(accessRightSet);
        ctx.scope().setResourceAccessRights(accessRightSet);
        return new Value();
    }

    @Override
    public String toString() {
        StringBuilder ars = new StringBuilder();
        for (Expression ar : arExpr.getLiteral().getArrayLiteral().getArray()) {
            if (!ars.isEmpty()) {
                ars.append(", ");
            }

            ars.append(String.format("%s", ar));
        }

        return String.format("set resource access rights [%s]", ars);
    }
}
