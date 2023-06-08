package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.pap.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static gov.nist.csd.pm.pap.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.pap.op.obligation.ObligationOp.AUTHOR_OPERAND;
import static gov.nist.csd.pm.pap.op.obligation.ObligationOp.RULES_OPERAND;


public class DeleteRuleStatement extends OperationStatement {

    private final Expression ruleExpr;
    private final Expression oblExpr;

    public DeleteRuleStatement(Expression ruleExpr, Expression oblExpr) {
        super(new UpdateObligationOp());
        this.ruleExpr = ruleExpr;
        this.oblExpr = oblExpr;
    }

    @Override
    public Map<String, Object> prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        String ruleName = ruleExpr.execute(ctx, pap).getStringValue();
        String oblName = oblExpr.execute(ctx, pap).getStringValue();

        Obligation obligation = pap.query().obligations().getObligation(oblName);
        List<Rule> rules = new ArrayList<>();
        for (Rule rule : obligation.getRules()) {
            if (rule.getName().equals(ruleName)) {
                continue;
            }

            rules.add(rule);
        }

        return Map.of(
                AUTHOR_OPERAND, obligation.getAuthor(),
                NAME_OPERAND, obligation.getName(),
                RULES_OPERAND, rules
        );
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("delete rule %s from obligation %s", ruleExpr, oblExpr);
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

    static class UpdateObligationOp extends Operation<Void> {

        public UpdateObligationOp() {
            super(
                    "delete_rule",
                    List.of(AUTHOR_OPERAND, NAME_OPERAND, RULES_OPERAND)
            );
        }

        @Override
        public void canExecute(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {
            new DeleteObligationOp()
                    .canExecute(pap, userCtx, operands);
            new CreateObligationOp()
                    .canExecute(pap, userCtx, operands);
        }

        @Override
        public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
            String author = (String) operands.get(AUTHOR_OPERAND);
            String name = (String) operands.get(NAME_OPERAND);
            List<Rule> rules = (List<Rule>) operands.get(RULES_OPERAND);

            pap.modify().obligations().deleteObligation(name);
            pap.modify().obligations().createObligation(author, name, rules);

            return null;
        }
    }
}
