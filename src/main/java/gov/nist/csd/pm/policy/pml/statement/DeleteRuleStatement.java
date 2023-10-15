package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

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
        String ruleName = ruleExpr.execute(ctx, policy).getStringValue();
        String oblName = oblExpr.execute(ctx, policy).getStringValue();

        Obligation obligation = policy.obligations().get(oblName);
        obligation.deleteRule(ruleName);

        policy.obligations().update(
                obligation.getAuthor(),
                obligation.getName(),
                obligation.getRules().toArray(new Rule[]{})
        );

        return new VoidValue();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("delete rule %s from obligation %s", ruleExpr, oblExpr);
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
