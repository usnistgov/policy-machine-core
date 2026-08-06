package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.DissociateStatement;


/**
 * Compiles a PML dissociate ... from ... statement into a {@link DissociateStatement}.
 */
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
