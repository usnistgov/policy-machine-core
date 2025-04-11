package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.pap.pml.exception.ArgTypeNotCastableException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.exception.UnknownPatternException;
import gov.nist.csd.pm.pap.function.op.obligation.ObligationOp.ObligationOpArgs;
import gov.nist.csd.pm.pap.obligation.EventPattern;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
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

        // execute the create rule statements and add to obligation
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
        try {
            return new CreateObligationStatement(
                new StringLiteralExpression(obligation.getName()),
                createRuleStatementsFromObligation(obligation.getRules())
            );
        } catch (UnknownPatternException | ArgTypeNotCastableException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<CreateRuleStatement> createRuleStatementsFromObligation(List<Rule> rules) throws
                                                                                                  UnknownPatternException {
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
