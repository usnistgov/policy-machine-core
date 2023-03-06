package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.List;

public class SetResourceAccessRightsStatement extends PALStatement{

    private final List<Expression> arExprList;

    public SetResourceAccessRightsStatement(List<Expression> arExprList) {
        this.arExprList = arExprList;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        AccessRightSet accessRightSet = new AccessRightSet();
        for (Expression e : arExprList) {
            accessRightSet.add(e.execute(ctx, policyAuthor).getStringValue());
        }

        policyAuthor.setResourceAccessRights(accessRightSet);
        ctx.scope().setResourceAccessRights(accessRightSet);
        return new Value();
    }

    @Override
    public String toString() {
        StringBuilder ars = new StringBuilder();
        for (Expression ar : arExprList) {
            if (!ars.isEmpty()) {
                ars.append(", ");
            }

            ars.append(String.format("%s", ar));
        }

        return String.format("set resource access rights %s;", ars);
    }
}
