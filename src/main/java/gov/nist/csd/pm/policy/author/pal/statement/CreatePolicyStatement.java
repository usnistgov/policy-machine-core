package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.Objects;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;

public class CreatePolicyStatement extends PALStatement {

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
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        policyAuthor.graph().createPolicyClass(name.execute(ctx, policyAuthor).getStringValue(), noprops());

        return new Value();
    }
}
