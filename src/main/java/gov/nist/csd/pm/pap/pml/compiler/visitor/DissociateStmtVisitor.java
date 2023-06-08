package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.statement.operation.DissociateStatement;
import gov.nist.csd.pm.pap.pml.type.Type;

public class DissociateStmtVisitor extends PMLBaseVisitor<DissociateStatement> {

    public DissociateStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public DissociateStatement visitDissociateStatement(PMLParser.DissociateStatementContext ctx) {
        Expression ua = Expression.compile(visitorCtx, ctx.ua, Type.string());
        Expression target = Expression.compile(visitorCtx, ctx.target, Type.string());
        return new DissociateStatement(ua, target);
    }
}
