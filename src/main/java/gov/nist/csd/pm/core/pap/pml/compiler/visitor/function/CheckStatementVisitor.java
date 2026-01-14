package gov.nist.csd.pm.core.pap.pml.compiler.visitor.function;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CheckStatement;
import java.util.List;

public class CheckStatementVisitor extends PMLBaseVisitor<CheckStatement> {
    public CheckStatementVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CheckStatement visitCheckStatement(PMLParser.CheckStatementContext ctx) {
        Expression<List<String>> arExpr = ExpressionVisitor.compile(visitorCtx, ctx.ar, ListType.of(STRING_TYPE));
        Expression<List<String>> targetExpr = ExpressionVisitor.compile(visitorCtx, ctx.target, ListType.of(STRING_TYPE));

        return new CheckStatement(arExpr, targetExpr);
    }
}
