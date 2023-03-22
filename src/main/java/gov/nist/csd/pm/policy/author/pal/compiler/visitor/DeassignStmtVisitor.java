package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.DeassignStatement;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;

public class DeassignStmtVisitor extends PALBaseVisitor<DeassignStatement> {

    private final VisitorContext visitorCtx;

    public DeassignStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public DeassignStatement visitDeassignStmt(PALParser.DeassignStmtContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.childNode, Type.string());
        Expression deassignFrom = Expression.compile(visitorCtx, ctx.parentNode, Type.string());
        return new DeassignStatement(name, deassignFrom);
    }
}