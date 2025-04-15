package gov.nist.csd.pm.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.pap.function.arg.type.Type.BOOLEAN_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.listType;
import static gov.nist.csd.pm.pap.function.arg.type.Type.mapType;

import gov.nist.csd.pm.common.prohibition.ProhibitionSubjectType;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateProhibitionStatement;
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
        if (ctx.USER() != null) {
            type = ProhibitionSubjectType.USER;
        } else if (ctx.USER_ATTRIBUTE() != null) {
            type = ProhibitionSubjectType.USER_ATTRIBUTE;
        }

        Expression<List<String>> accessRights = ExpressionVisitor.compile(visitorCtx, ctx.accessRights, listType(STRING_TYPE));

        boolean isIntersection = ctx.INTERSECTION() != null;

        Expression<Map<String, Boolean>> cc = ExpressionVisitor.compile(visitorCtx, ctx.containers, mapType(STRING_TYPE, BOOLEAN_TYPE));

        return new CreateProhibitionStatement(name, subject, type, accessRights, isIntersection, cc);
    }
}
