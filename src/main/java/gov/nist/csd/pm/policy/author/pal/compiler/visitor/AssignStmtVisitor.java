package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.AssignStatement;

public class AssignStmtVisitor extends PALBaseVisitor<AssignStatement> {

    private final VisitorContext visitorCtx;

    public AssignStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public AssignStatement visitAssignStmt(PALParser.AssignStmtContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.child,
                Type.string());
        Expression assignTo = Expression.compile(visitorCtx, ctx.assignTo,
                Type.string(), Type.array(Type.any()));

        return new AssignStatement(name, assignTo);
    }
}
