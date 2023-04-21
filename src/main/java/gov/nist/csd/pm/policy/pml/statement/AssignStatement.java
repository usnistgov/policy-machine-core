package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;
import java.util.Objects;

public class AssignStatement extends PALStatement {

    private final Expression child;
    private final Expression assignTo;

    public AssignStatement(Expression child, Expression assignTo) {
        this.child = child;
        this.assignTo = assignTo;
    }

    public Expression getChild() {
        return child;
    }

    public Expression getAssignTo() {
        return assignTo;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        Value childValue = this.child.execute(ctx, policy);
        Value assignToValue = this.assignTo.execute(ctx, policy);

        String childStringValue = childValue.getStringValue();

        if (assignToValue.isString()) {
            String parent = assignToValue.getStringValue();
            policy.assign(childStringValue, parent);
        } else if (assignToValue.isArray()) {
            List<Value> valueArr = assignToValue.getArrayValue();
            for (Value value : valueArr) {
                String parent = value.getStringValue();
                policy.assign(childStringValue, parent);
            }
        }

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("assign %s to %s;", child, assignTo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignStatement that = (AssignStatement) o;
        return Objects.equals(child, that.child) && Objects.equals(assignTo, that.assignTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child, assignTo);
    }
}
