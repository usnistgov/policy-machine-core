package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SetResourceAccessRightsStatement extends PALStatement{

    private final List<Expression> arExprList;

    public SetResourceAccessRightsStatement(List<Expression> arExprList) {
        this.arExprList = arExprList;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        AccessRightSet accessRightSet = new AccessRightSet();
        for (Expression e : arExprList) {
            Value v = e.execute(ctx, policyAuthor);
            switch (v.getType().toString()) {
                case "string":
                    accessRightSet.add(e.execute(ctx, policyAuthor).getStringValue());
                    break;
                case "[]string":
                    List<String> l = Arrays.stream(v.getArrayValue())
                        .map(Value::getStringValue)
                        .collect(Collectors.toList());

                    accessRightSet.addAll(l);
                    break;
                default:
                    throw new PMException("Illegal value type for access right: " + v.getType().toString());
            }

        }

        policyAuthor.graph().setResourceAccessRights(accessRightSet);
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
