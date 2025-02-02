package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.obligation.EventPattern;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.exception.UnknownPatternException;
import gov.nist.csd.pm.common.op.obligation.CreateObligationOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pap.pml.expression.Expression;

import java.util.*;

import static gov.nist.csd.pm.common.op.graph.GraphOp.*;
import static gov.nist.csd.pm.common.op.obligation.ObligationOp.*;


public class CreateObligationStatement extends OperationStatement<Void> {

    private Expression name;
    private List<CreateRuleStatement> ruleStmts;

    public CreateObligationStatement(Expression name, List<CreateRuleStatement> ruleStmts) {
        super(new CreateObligationOp());
        this.name = name;
        this.ruleStmts = ruleStmts;
    }

    public Expression getName() {
        return name;
    }

    public List<CreateRuleStatement> getRuleStmts() {
        return ruleStmts;
    }

    @Override
    public Map<String, Object> prepareOperands(ExecutionContext ctx, PAP pap)
            throws PMException {
        String nameStr = name.execute(ctx, pap).getStringValue();

        // execute the create rule statements and add to obligation
        List<Rule> rules = new ArrayList<>();
        for (CreateRuleStatement createRuleStmt : ruleStmts) {
            Rule rule = createRuleStmt.execute(ctx, pap).getRuleValue();
            rules.add(rule);
        }

        return Map.of(AUTHOR_OPERAND, ctx.author().getUser(), NAME_OPERAND, nameStr, RULES_OPERAND, rules);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateObligationStatement that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(ruleStmts, that.ruleStmts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ruleStmts);
    }

    public static CreateObligationStatement fromObligation(Obligation obligation) {
        try {
            return new CreateObligationStatement(
                    new StringLiteral(obligation.getName()),
                    createRuleStatementsFromObligation(obligation.getRules())
            );
        } catch (UnknownPatternException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<CreateRuleStatement> createRuleStatementsFromObligation(List<Rule> rules) throws UnknownPatternException {
        List<CreateRuleStatement> createRuleStatements = new ArrayList<>();

        for (Rule rule : rules) {
            EventPattern event = rule.getEventPattern();

            CreateRuleStatement createRuleStatement = new CreateRuleStatement(
                    new StringLiteral(rule.getName()),
                    event.getSubjectPattern(),
                    event.getOperationPattern(),
                    event.getOperandPatterns(),
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
