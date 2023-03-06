package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.Objects;

public class DeleteRuleStatement extends PALStatement {

    private final Expression ruleExpr;
    private final Expression oblExpr;

    public DeleteRuleStatement(Expression ruleExpr, Expression oblExpr) {
        this.ruleExpr = ruleExpr;
        this.oblExpr = oblExpr;
    }

    public Expression getRuleExpr() {
        return ruleExpr;
    }

    public Expression getOblExpr() {
        return oblExpr;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        String ruleLabel = ruleExpr.execute(ctx, policyAuthor).getStringValue();
        String oblLabel = oblExpr.execute(ctx, policyAuthor).getStringValue();

        Obligation obligation = policyAuthor.getObligation(oblLabel);
        obligation.deleteRule(ruleLabel);

        policyAuthor.updateObligation(
                obligation.getAuthor(),
                obligation.getLabel(),
                obligation.getRules().toArray(new Rule[]{})
        );

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("delete rule %s from obligation %s;", ruleExpr, oblExpr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteRuleStatement that = (DeleteRuleStatement) o;
        return Objects.equals(ruleExpr, that.ruleExpr) && Objects.equals(oblExpr, that.oblExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleExpr, oblExpr);
    }
}
