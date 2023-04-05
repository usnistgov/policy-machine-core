package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

public class SetResourceAccessRightsStatement extends PALStatement{

    private final Expression arExpr;

    public SetResourceAccessRightsStatement(Expression arExprList) {
        this.arExpr = arExprList;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        Value arValue = arExpr.execute(ctx, policyAuthor);
        AccessRightSet accessRightSet = new AccessRightSet();
        for (Value v : arValue.getArrayValue()) {
            accessRightSet.add(v.getStringValue());
        }

        policyAuthor.setResourceAccessRights(accessRightSet);
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

        return String.format("set resource access rights [%s];", ars);
    }
}
