package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.obligation.ObligationOp.ObligationOpArgs;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.pap.function.op.obligation.DeleteObligationOp;
import gov.nist.csd.pm.pap.function.op.obligation.ObligationOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    static class UpdateObligationOp extends ObligationOp<ObligationOpArgs> {

        public UpdateObligationOp() {
            super(
                "delete_rule",
                List.of(AUTHOR_ARG, NAME_ARG, RULES_ARG),
                ""
            );
        }

        @Override
        public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ObligationOpArgs args) throws PMException {
            new DeleteObligationOp()
                .canExecute(privilegeChecker, userCtx, args);
            new CreateObligationOp()
                .canExecute(privilegeChecker, userCtx, args);
        }

        @Override
        public Void execute(PAP pap, ObligationOpArgs args) throws PMException {
            long author = args.getAuthorId();
            String name = args.getName();
            List<Rule> rules = args.getRules();

            // delete the obligation
            pap.modify().obligations().deleteObligation(name);

            // recreate it with updated ruleset
            pap.modify().obligations().createObligation(author, name, rules);

            return null;
        }

        @Override
        public ObligationOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
            Long authorId = prepareArg(AUTHOR_ARG, argsMap);
            String name = prepareArg(NAME_ARG, argsMap);
            List<Rule> rules = prepareArg(RULES_ARG, argsMap);
            return new ObligationOpArgs(authorId, name, rules);
        }
    }
}
