package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateProhibitionStatement;
import java.util.ArrayList;
import java.util.List;

public class CreateProhibitionStmtVisitor extends PMLBaseVisitor<CreateProhibitionStatement> {

    public CreateProhibitionStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreateProhibitionStatement visitCreateProhibitionStatement(PMLParser.CreateProhibitionStatementContext ctx) {
        Expression<String> name = ExpressionVisitor.compile(visitorCtx, ctx.name, STRING_TYPE);
        Expression<String> node = ExpressionVisitor.compile(visitorCtx, ctx.node, STRING_TYPE);

        boolean isConjunctive = ctx.type.getType() == PMLParser.CONJ;
        boolean isProcessProhibition = ctx.entity.getType() == PMLParser.PROCESS;

        Expression<List<String>> arset = ExpressionVisitor.compile(visitorCtx, ctx.arset, ListType.of(STRING_TYPE));
        Expression<List<String>> inclusionSet = new ArrayLiteralExpression<>(new ArrayList<>(), STRING_TYPE);
        if (ctx.inclusionSet != null) {
            inclusionSet = ExpressionVisitor.compile(visitorCtx, ctx.inclusionSet, ListType.of(STRING_TYPE));
        }

        Expression<List<String>> exclusionSet = new ArrayLiteralExpression<>(new ArrayList<>(), STRING_TYPE);
        if (ctx.exclusionSet != null) {
            exclusionSet = ExpressionVisitor.compile(visitorCtx, ctx.exclusionSet, ListType.of(STRING_TYPE));
        }

        if (isProcessProhibition) {
            Expression<String> process = ExpressionVisitor.compile(visitorCtx, ctx.process, STRING_TYPE);
            return CreateProhibitionStatement.processProhibition(name, node, process, arset, inclusionSet, exclusionSet, isConjunctive);
        }

        return CreateProhibitionStatement.nodeProhibition(name, node, arset, inclusionSet, exclusionSet, isConjunctive);
    }
}
