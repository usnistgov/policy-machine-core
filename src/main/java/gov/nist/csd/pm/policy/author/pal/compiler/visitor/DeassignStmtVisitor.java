package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.statement.NameExpression;
import gov.nist.csd.pm.policy.author.pal.statement.DeassignStatement;

public class DeassignStmtVisitor extends PALBaseVisitor<DeassignStatement> {

    private final VisitorContext visitorCtx;

    public DeassignStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public DeassignStatement visitDeassignStmt(PALParser.DeassignStmtContext ctx) {
        NameExpression name = NameExpression.compile(visitorCtx, ctx.child);
        NameExpression deassignFrom = NameExpression.compile(visitorCtx, ctx.parent);
        return new DeassignStatement(name, deassignFrom);
    }
}