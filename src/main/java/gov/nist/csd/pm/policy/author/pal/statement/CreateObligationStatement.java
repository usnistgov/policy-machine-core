package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static gov.nist.csd.pm.policy.author.pal.PALFormatter.statementsToString;

public class CreateObligationStatement extends PALStatement {

    private final Expression labelExpr;
    private final List<CreateRuleStatement> ruleStmts;

    public CreateObligationStatement(Expression labelExpr, List<CreateRuleStatement> ruleStmts) {
        this.labelExpr = labelExpr;
        this.ruleStmts = ruleStmts;
    }

    public Expression getLabelExpr() {
        return labelExpr;
    }

    public List<CreateRuleStatement> getRuleStmts() {
        return ruleStmts;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        UserContext author = ctx.getAuthor();
        String label = labelExpr.execute(ctx, policyAuthor).getStringValue();

        // execute the create rule statements and add to obligation
        List<Rule> rules = new ArrayList<>();
        for (CreateRuleStatement createRuleStmt : ruleStmts) {
            Value createRuleValue = createRuleStmt.execute(ctx, policyAuthor);
            Rule rule = createRuleValue.getRule();
            rules.add(rule);
        }

        policyAuthor.obligations().create(author, label, rules.toArray(rules.toArray(Rule[]::new)));

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("create obligation %s {%s}", labelExpr, statementsToString(ruleStmts));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateObligationStatement that = (CreateObligationStatement) o;
        return Objects.equals(labelExpr, that.labelExpr) && Objects.equals(ruleStmts, that.ruleStmts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labelExpr, ruleStmts);
    }
}
