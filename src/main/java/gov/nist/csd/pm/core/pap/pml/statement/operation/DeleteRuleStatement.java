package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.op.obligation.ObligationOp.ObligationOpArgs;
import gov.nist.csd.pm.core.pap.function.op.obligation.UpdateObligationOp;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeleteRuleStatement extends OperationStatement<ObligationOpArgs> {

    private final Expression<String> ruleExpr;
    private final Expression<String> oblExpr;

    public DeleteRuleStatement(Expression<String> ruleExpr, Expression<String> oblExpr) {
        super(new UpdateObligationOp());
        this.ruleExpr = ruleExpr;
        this.oblExpr = oblExpr;
    }

    @Override
    public ObligationOpArgs prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String ruleName = ruleExpr.execute(ctx, pap);
        String oblName = oblExpr.execute(ctx, pap);

        Obligation obligation = pap.query().obligations().getObligation(oblName);
        List<Rule> rules = new ArrayList<>();
        for (Rule rule : obligation.getRules()) {
            if (rule.getName().equals(ruleName)) {
                continue;
            }

            rules.add(rule);
        }

        // even though we are updating an obligation, use the same author id as this statement
        // can only be called from within an obligation response which will be executed by the
        // author anyways
        return new ObligationOpArgs(obligation.getAuthorId(), obligation.getName(), new ArrayList<>(rules));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeleteRuleStatement that)) return false;
        return Objects.equals(ruleExpr, that.ruleExpr) && Objects.equals(oblExpr, that.oblExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleExpr, oblExpr);
    }


    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("delete rule %s from obligation %s", ruleExpr, oblExpr);
    }
}
