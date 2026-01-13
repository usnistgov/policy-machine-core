package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;


import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.operation.AssociateStatement;
import java.util.List;


public class AssociateStmtVisitor extends PMLBaseVisitor<AssociateStatement> {

    public AssociateStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public AssociateStatement visitAssociateStatement(PMLParser.AssociateStatementContext ctx) {
        Expression<String> ua = ExpressionVisitor.compile(visitorCtx, ctx.ua, STRING_TYPE);
        Expression<String> target = ExpressionVisitor.compile(visitorCtx, ctx.target, STRING_TYPE);
        Expression<List<String>> accessRights = ExpressionVisitor.compile(visitorCtx, ctx.accessRights, ListType.of(STRING_TYPE));

        return new AssociateStatement(ua, target, accessRights);
    }
}
