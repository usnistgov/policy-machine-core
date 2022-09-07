package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.CreateObligationStatement;
import gov.nist.csd.pm.policy.author.pal.statement.CreateRuleStatement;

import java.util.ArrayList;
import java.util.List;

public class CreateObligationStmtVisitor extends PALBaseVisitor<CreateObligationStatement> {

    private final VisitorContext visitorCtx;

    public CreateObligationStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public CreateObligationStatement visitCreateObligationStmt(PALParser.CreateObligationStmtContext ctx) {
        Expression label = Expression.compile(visitorCtx, ctx.label, Type.string());

        List<CreateRuleStatement> ruleStmts = new ArrayList<>();
        for (PALParser.CreateRuleStmtContext ruleStmt : ctx.createRuleStmt()) {
            CreateRuleStatement createRuleStmt = new CreateRuleStmtVisitor(visitorCtx)
                    .visitCreateRuleStmt(ruleStmt);
            ruleStmts.add(createRuleStmt);
        }

        return new CreateObligationStatement(label, ruleStmts);
    }
}
