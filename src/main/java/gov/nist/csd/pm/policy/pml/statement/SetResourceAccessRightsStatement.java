package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;

import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

import java.util.Objects;


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

        return new VoidValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SetResourceAccessRightsStatement that = (SetResourceAccessRightsStatement) o;
        return Objects.equals(arExpr, that.arExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arExpr);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("set resource access rights %s", arExpr);
    }
}
