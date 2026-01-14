package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.function.op.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.obligation.ObligationOp.AUTHOR_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.obligation.ObligationOp.RULES_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.core.pap.obligation.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.ObligationResponse;
import gov.nist.csd.pm.core.pap.obligation.PMLObligationResponse;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateObligationStatement extends OperationStatement {

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
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String nameStr = name.execute(ctx, pap);

        // execute the create rule statements and add to obligation
        List<Rule> rules = new ArrayList<>();
        for (CreateRuleStatement createRuleStmt : ruleStmts) {
            Rule rule = createRuleStmt.execute(ctx, pap);
            rules.add(rule);
        }

        return new Args()
            .put(AUTHOR_PARAM, ctx.author().getUser())
            .put(NAME_PARAM, nameStr)
            .put(RULES_PARAM, new ArrayList<>(rules));
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
            ObligationResponse response = rule.getResponse();
            if (!(response instanceof PMLObligationResponse pmlObligationResponse)) {
                throw new IllegalStateException("cannot convert rule " + rule.getName() + " to PML because it has a JavaObligationResponse");
            }

            CreateRuleStatement createRuleStatement = new CreateRuleStatement(
                new StringLiteralExpression(rule.getName()),
                event.getSubjectPattern(),
                event.getOperationPattern(),
                event.getArgPatterns(),
                new CreateRuleStatement.ResponseBlock(
                    pmlObligationResponse.getEventCtxVariable(),
                    pmlObligationResponse.getStatements()
                )
            );

            createRuleStatements.add(createRuleStatement);
        }

        return createRuleStatements;
    }
}
