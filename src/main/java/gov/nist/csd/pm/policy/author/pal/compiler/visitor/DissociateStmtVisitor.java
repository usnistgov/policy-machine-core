package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.DissociateStatement;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;

public class DissociateStmtVisitor extends PALBaseVisitor<DissociateStatement> {

    private final VisitorContext visitorCtx;

    public DissociateStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public DissociateStatement visitDissociateStmt(PALParser.DissociateStmtContext ctx) {
        Expression ua = Expression.compile(visitorCtx, ctx.ua, Type.string());
        Expression target = Expression.compile(visitorCtx, ctx.target, Type.string());
        return new DissociateStatement(ua, target);
    }
}
