package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;

import java.util.Objects;

public class AssociateStatement extends PMLStatement {

    private final Expression ua;
    private final Expression target;
    private final Expression accessRights;

    public AssociateStatement(Expression ua, Expression target, Expression accessRights) {
        this.ua = ua;
        this.target = target;
        this.accessRights = accessRights;
    }

    public Expression getUa() {
        return ua;
    }

    public Expression getTarget() {
        return target;
    }

    public Expression getAccessRights() {
        return accessRights;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        Value uaValue = ua.execute(ctx, policy);
        Value targetValue = target.execute(ctx, policy);
        Value permissionsValue = accessRights.execute(ctx, policy);

        AccessRightSet accessRightSet = new AccessRightSet();
        for (Value v : permissionsValue.getArrayValue()) {
            accessRightSet.add(v.getStringValue());
        }

        policy.graph().associate(
                uaValue.getStringValue(),
                targetValue.getStringValue(),
                accessRightSet
        );

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("associate %s and %s with %s",
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
