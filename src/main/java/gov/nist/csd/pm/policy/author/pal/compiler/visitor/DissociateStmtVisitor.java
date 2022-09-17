package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.statement.NameExpression;
import gov.nist.csd.pm.policy.author.pal.statement.DissociateStatement;

public class DissociateStmtVisitor extends PALBaseVisitor<DissociateStatement> {

    private final VisitorContext visitorCtx;

    public DissociateStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public DissociateStatement visitDissociateStmt(PALParser.DissociateStmtContext ctx) {
        NameExpression ua = NameExpression.compile(visitorCtx, ctx.ua);
        NameExpression target = NameExpression.compile(visitorCtx, ctx.target);
        return new DissociateStatement(ua, target);
    }
}
