package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.statement.CreateObligationStatement;
import gov.nist.csd.pm.policy.pml.statement.CreateRuleStatement;

import java.util.ArrayList;
import java.util.List;

public class CreateObligationStmtVisitor extends PMLParserBaseVisitor<PMLStatement> {

    private final VisitorContext visitorCtx;

    public CreateObligationStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public PMLStatement visitCreateObligationStatement(PMLParser.CreateObligationStatementContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.expression(), Type.string());

        List<CreateRuleStatement> ruleStmts = new ArrayList<>();
        for (PMLParser.CreateRuleStatementContext ruleStmt : ctx.createRuleStatement()) {
            CreateRuleStatement createRuleStmt = (CreateRuleStatement) new CreateRuleStmtVisitor(visitorCtx)
                    .visitCreateRuleStatement(ruleStmt);
            ruleStmts.add(createRuleStmt);
        }

        return new CreateObligationStatement(name, ruleStmts);
    }
}
