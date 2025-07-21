package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.op.obligation.ObligationOp.ObligationOpArgs;
import gov.nist.csd.pm.core.pap.obligation.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateObligationStatement extends OperationStatement<ObligationOpArgs> {

    private final Expression<String> name;
    private final List<CreateRuleStatement> ruleStmts;

    public CreateObligationStatement(Expression<String> name, List<CreateRuleStatement> ruleStmts) {
        super(new CreateObligationOp());
        this.name = name;
        this.ruleStmts = ruleStmts;
    }

    public Expression<String> getName() {
        return name;
    }

    public List<CreateRuleStatement> getRuleStmts() {
        return ruleStmts;
    }

    @Override
    public ObligationOpArgs prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String nameStr = name.execute(ctx, pap);

        // execute the rule statements and add to obligation
        List<Rule> rules = new ArrayList<>();
        for (CreateRuleStatement createRuleStmt : ruleStmts) {
            Rule rule = createRuleStmt.execute(ctx, pap);
            rules.add(rule);
        }

        return new ObligationOpArgs(ctx.author().getUser(), nameStr, new ArrayList<>(rules));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CreateObligationStatement that))
            return false;
        return Objects.equals(name, that.name) && Objects.equals(ruleStmts, that.ruleStmts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ruleStmts);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        for (CreateRuleStatement createRuleStatement : ruleStmts) {
            sb.append(createRuleStatement.toFormattedString(indentLevel+1)).append("\n\n");
        }

        String indent = indent(indentLevel);
        return String.format(
            """
            %screate obligation %s {
            %s%s}""", indent, name, sb, indent);
    }

    public static CreateObligationStatement fromObligation(Obligation obligation) {
        return new CreateObligationStatement(
            new StringLiteralExpression(obligation.getName()),
            createRuleStatementsFromObligation(obligation.getRules())
        );
    }

    private static List<CreateRuleStatement> createRuleStatementsFromObligation(List<Rule> rules) {
        List<CreateRuleStatement> createRuleStatements = new ArrayList<>();

        for (Rule rule : rules) {
            EventPattern event = rule.getEventPattern();

            CreateRuleStatement createRuleStatement = new CreateRuleStatement(
                new StringLiteralExpression(rule.getName()),
                event.getSubjectPattern(),
                event.getOperationPattern(),
                event.getArgPatterns(),
                new CreateRuleStatement.ResponseBlock(
                    rule.getResponse().getEventCtxVariable(),
                    rule.getResponse().getStatements()
                )
            );

            createRuleStatements.add(createRuleStatement);
        }

        return createRuleStatements;
    }
}
