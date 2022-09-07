package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.Objects;

public class CreateStatement extends PALStatement {

    private final PALStatement statement;

    public CreateStatement(PALStatement statement) {
        this.statement = statement;
    }

    public PALStatement getStatement() {
        return statement;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        return this.statement.execute(ctx, policyAuthor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateStatement that = (CreateStatement) o;
        return Objects.equals(statement, that.statement);
    }

    @Override
    public int hashCode() {
        return statement.hashCode();
    }
}
