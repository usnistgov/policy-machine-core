package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;
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
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        Value childValue = child.execute(ctx, policy);
        Value deassignFromValue = deassignFrom.execute(ctx, policy);

        String childStringValue = childValue.getStringValue();

        if (deassignFromValue.isString()) {
            String parent = deassignFromValue.getStringValue();
            policy.deassign(childStringValue, parent);
        } else {
            List<Value> valueArr = deassignFromValue.getArrayValue();
            for (Value value : valueArr) {
                String parent = value.getStringValue();
                policy.deassign(childStringValue, parent);
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
