package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.Objects;

public class DeassignStatement extends PALStatement {

    private final Expression child;
    private final Expression deassignFrom;

    public DeassignStatement(Expression child, Expression deassignFrom) {
        this.child = child;
        this.deassignFrom = deassignFrom;
    }

    public Expression getChild() {
        return child;
    }

    public Expression getDeassignFrom() {
        return deassignFrom;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        Value childValue = child.execute(ctx, policyAuthor);
        Value deassignFromValue = deassignFrom.execute(ctx, policyAuthor);

        String childStringValue = childValue.getStringValue();

        if (deassignFromValue.isString()) {
            String parent = deassignFromValue.getStringValue();
            policyAuthor.deassign(childStringValue, parent);
        } else {
            Value[] valueArr = deassignFromValue.getArrayValue();
            for (Value value : valueArr) {
                String parent = value.getStringValue();
                policyAuthor.deassign(childStringValue, parent);
            }
        }

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("deassign %s from %s;",
                child,
                deassignFrom
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeassignStatement that = (DeassignStatement) o;
        return Objects.equals(child, that.child) && Objects.equals(deassignFrom, that.deassignFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child, deassignFrom);
    }
}
