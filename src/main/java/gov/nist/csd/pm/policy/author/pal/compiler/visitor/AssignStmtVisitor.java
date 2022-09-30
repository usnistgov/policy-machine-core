package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.statement.NameExpression;
import gov.nist.csd.pm.policy.author.pal.statement.AssignStatement;

public class AssignStmtVisitor extends PALBaseVisitor<AssignStatement> {

    private final VisitorContext visitorCtx;

    public AssignStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public AssignStatement visitAssignStmt(PALParser.AssignStmtContext ctx) {
        NameExpression child = NameExpression.compile(visitorCtx, ctx.child);
        NameExpression parent = NameExpression.compile(visitorCtx, ctx.parent);

        return new AssignStatement(child, parent);
    }
}
