package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.DeassignStatement;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;

import java.util.List;

public class DeassignStmtVisitor extends PALBaseVisitor<DeassignStatement> {

    private final VisitorContext visitorCtx;

    public DeassignStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public DeassignStatement visitDeassignStmt(PALParser.DeassignStmtContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.childNode, Type.string());
        Expression parents = Expression.compile(visitorCtx, ctx.parentNodes, Type.array(Type.string()));

        return new DeassignStatement(name, parents);
    }
}