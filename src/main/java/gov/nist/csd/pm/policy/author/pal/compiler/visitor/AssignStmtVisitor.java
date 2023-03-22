package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.AssignStatement;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;

public class AssignStmtVisitor extends PALBaseVisitor<AssignStatement> {

    private final VisitorContext visitorCtx;

    public AssignStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public AssignStatement visitAssignStmt(PALParser.AssignStmtContext ctx) {
        Expression child = Expression.compile(visitorCtx, ctx.childNode, Type.string());
        Expression parent = Expression.compile(visitorCtx, ctx.parentNode, Type.string());

        return new AssignStatement(child, parent);
    }
}
