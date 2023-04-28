package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.Objects;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;

public class CreatePolicyStatement extends PMLStatement {

    private final Expression name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreatePolicyStatement that = (CreatePolicyStatement) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public CreatePolicyStatement(Expression name) {
        this.name = name;
    }

    public Expression getName() {
        return name;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        policy.graph().createPolicyClass(name.execute(ctx, policy).getStringValue(), NO_PROPERTIES);

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("create policy class %s", name);
    }
}
