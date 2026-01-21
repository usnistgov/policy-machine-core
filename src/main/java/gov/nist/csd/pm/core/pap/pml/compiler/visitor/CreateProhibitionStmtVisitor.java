package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubjectType;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateProhibitionStatement;
import java.util.List;
import java.util.Map;

public class CreateProhibitionStmtVisitor extends PMLBaseVisitor<CreateProhibitionStatement> {

    public CreateProhibitionStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreateProhibitionStatement visitCreateProhibitionStatement(PMLParser.CreateProhibitionStatementContext ctx) {
        Expression<String> name = ExpressionVisitor.compile(visitorCtx, ctx.name, STRING_TYPE);
        Expression<String> subject = ExpressionVisitor.compile(visitorCtx, ctx.subject, STRING_TYPE);
        ProhibitionSubjectType type = ProhibitionSubjectType.PROCESS;
        if (ctx.U() != null) {
            type = ProhibitionSubjectType.USER;
        } else if (ctx.UA() != null) {
            type = ProhibitionSubjectType.USER_ATTRIBUTE;
        }

        Expression<List<String>> accessRights = ExpressionVisitor.compile(visitorCtx, ctx.accessRights, ListType.of(STRING_TYPE));

        boolean isIntersection = ctx.INTERSECTION() != null;

        Expression<Map<String, Boolean>> cc = ExpressionVisitor.compile(visitorCtx, ctx.containers, MapType.of(STRING_TYPE, BOOLEAN_TYPE));

        return new CreateProhibitionStatement(name, subject, type, accessRights, isIntersection, cc);
    }
}
