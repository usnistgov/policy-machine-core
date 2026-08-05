package gov.nist.ngac.pm.core.pap.pml.compiler.visitor.operation;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.RequireStatement;
import java.util.List;

public class RequireStatementVisitor extends PMLBaseVisitor<RequireStatement> {
    public RequireStatementVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public RequireStatement visitRequireStatement(PMLParser.RequireStatementContext ctx) {
        Expression<List<String>> arExpr = ExpressionVisitor.compile(visitorCtx, ctx.ar, ListType.of(STRING_TYPE));
        Expression<List<String>> targetExpr = ExpressionVisitor.compile(visitorCtx, ctx.target, ListType.of(STRING_TYPE));

        return new RequireStatement(arExpr, targetExpr);
    }
}
