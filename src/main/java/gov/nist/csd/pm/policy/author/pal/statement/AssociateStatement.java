package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.Objects;

public class AssociateStatement extends PALStatement {

    private final NameExpression ua;
    private final NameExpression target;
    private final Expression accessRights;

    public AssociateStatement(NameExpression ua, NameExpression target, Expression accessRights) {
        this.ua = ua;
        this.target = target;
        this.accessRights = accessRights;
    }

    public NameExpression getUa() {
        return ua;
    }

    public NameExpression getTarget() {
        return target;
    }

    public Expression getAccessRights() {
        return accessRights;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        Value uaValue = ua.execute(ctx, policyAuthor);
        Value targetValue = target.execute(ctx, policyAuthor);
        Value permissionsValue = accessRights.execute(ctx, policyAuthor);

        AccessRightSet accessRightSet = new AccessRightSet();
        for (Value v : permissionsValue.getArrayValue()) {
            accessRightSet.add(v.getStringValue());
        }

        policyAuthor.graph().associate(
                uaValue.getStringValue(),
                targetValue.getStringValue(),
                accessRightSet
        );

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("associate %s and %s with access rights %s;",
                ua, target, accessRights);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssociateStatement that = (AssociateStatement) o;
        return Objects.equals(ua, that.ua) && Objects.equals(target, that.target) && Objects.equals(accessRights, that.accessRights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ua, target, accessRights);
    }
}
