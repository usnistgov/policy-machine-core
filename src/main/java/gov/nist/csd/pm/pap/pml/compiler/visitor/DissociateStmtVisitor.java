package gov.nist.csd.pm.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.operation.DissociateStatement;


public class DissociateStmtVisitor extends PMLBaseVisitor<DissociateStatement> {

    public DissociateStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public DissociateStatement visitDissociateStatement(PMLParser.DissociateStatementContext ctx) {
        Expression<String> ua = ExpressionVisitor.compile(visitorCtx, ctx.ua, STRING_TYPE);
        Expression<String> target = ExpressionVisitor.compile(visitorCtx, ctx.target, STRING_TYPE);
        return new DissociateStatement(ua, target);
    }
}
