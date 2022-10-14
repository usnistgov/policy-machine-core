package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.Objects;

public class DissociateStatement extends PALStatement {

    private final Expression uaExpr;
    private final Expression targetExpr;

    public DissociateStatement(Expression uaExpr, Expression targetExpr) {
        this.uaExpr = uaExpr;
        this.targetExpr = targetExpr;
    }

    public Expression getUaExpr() {
        return uaExpr;
    }

    public Expression getTargetExpr() {
        return targetExpr;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        String ua = uaExpr.execute(ctx, policyAuthor).getStringValue();
        String target = targetExpr.execute(ctx, policyAuthor).getStringValue();

        policyAuthor.graph().dissociate(ua, target);

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("dissociate %s and %s;", uaExpr, targetExpr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DissociateStatement that = (DissociateStatement) o;
        return Objects.equals(uaExpr, that.uaExpr) && Objects.equals(targetExpr, that.targetExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uaExpr, targetExpr);
    }
}
