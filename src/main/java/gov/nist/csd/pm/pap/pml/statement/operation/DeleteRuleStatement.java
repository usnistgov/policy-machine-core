package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.pap.executable.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.pap.executable.op.obligation.ObligationOp;
import gov.nist.csd.pm.pap.executable.op.obligation.RuleList;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.operation.DeleteRuleStatement.UpdateObligationOp;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeleteRuleStatement extends OperationStatement<UpdateObligationOp> {

    private final Expression ruleExpr;
    private final Expression oblExpr;

    public DeleteRuleStatement(Expression ruleExpr, Expression oblExpr) {
        super(new UpdateObligationOp());
        this.ruleExpr = ruleExpr;
        this.oblExpr = oblExpr;
    }

    @Override
    public ActualArgs prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
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

        // even though we are updating an obligation, use the same author id as this statement
        // can only be called from within an obligation response which will be executed by the
        // author anyways
        return op.actualArgs(obligation.getAuthorId(), obligation.getName(), new RuleList(rules));
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

    static class UpdateObligationOp extends ObligationOp {

        public UpdateObligationOp() {
            super(
                "delete_rule",
                ""
            );
        }

        @Override
        public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs operands) throws PMException {
            new DeleteObligationOp()
                .canExecute(privilegeChecker, userCtx, operands);
            new CreateObligationOp()
                .canExecute(privilegeChecker, userCtx, operands);
        }

        @Override
        public Void execute(PAP pap, ActualArgs actualArgs) throws PMException {
            long author = actualArgs.get(AUTHOR_ARG);
            String name = actualArgs.get(NAME_ARG);
            List<Rule> rules = actualArgs.get(RULES_ARG);

            // delete the obligation
            pap.modify().obligations().deleteObligation(name);

            // recreate it with updated ruleset
            pap.modify().obligations().createObligation(author, name, rules);

            return null;
        }
    }
}
