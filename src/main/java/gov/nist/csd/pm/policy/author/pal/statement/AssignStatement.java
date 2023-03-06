package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

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
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        Value childValue = this.child.execute(ctx, policyAuthor);
        Value assignToValue = this.assignTo.execute(ctx, policyAuthor);

        String childStringValue = childValue.getStringValue();

        if (assignToValue.isString()) {
            String parent = assignToValue.getStringValue();
            policyAuthor.assign(childStringValue, parent);
        } else if (assignToValue.isArray()) {
            Value[] valueArr = assignToValue.getArrayValue();
            for (Value value : valueArr) {
                String parent = value.getStringValue();
                policyAuthor.assign(childStringValue, parent);
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
