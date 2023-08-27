package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;

import java.util.Objects;

public class DeleteRuleStatement extends PMLStatement {

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
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        String ruleId = ruleExpr.execute(ctx, policy).getStringValue();
        String oblId = oblExpr.execute(ctx, policy).getStringValue();

        Obligation obligation = policy.obligations().get(oblId);
        obligation.deleteRule(ruleId);

        policy.obligations().update(
                obligation.getAuthor(),
                obligation.getId(),
                obligation.getRules().toArray(new Rule[]{})
        );

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("delete rule %s from obligation %s", ruleExpr, oblExpr);
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
