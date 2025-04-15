package gov.nist.csd.pm.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.listType;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.operation.AssociateStatement;


public class AssociateStmtVisitor extends PMLBaseVisitor<AssociateStatement> {

    public AssociateStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public AssociateStatement visitAssociateStatement(PMLParser.AssociateStatementContext ctx) {
        Expression ua = ExpressionVisitor.compile(visitorCtx, ctx.ua, STRING_TYPE);
        Expression target = ExpressionVisitor.compile(visitorCtx, ctx.target, STRING_TYPE);
        Expression accessRights = ExpressionVisitor.compile(visitorCtx, ctx.accessRights, listType(STRING_TYPE));

        return new AssociateStatement(ua, target, accessRights);
    }
}
