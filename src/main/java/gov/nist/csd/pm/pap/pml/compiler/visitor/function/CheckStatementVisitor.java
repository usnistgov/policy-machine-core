package gov.nist.csd.pm.pap.pml.compiler.visitor.function;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.operation.CheckStatement;
import gov.nist.csd.pm.pap.pml.type.Type;

public class CheckStatementVisitor extends PMLBaseVisitor<CheckStatement> {
    public CheckStatementVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CheckStatement visitCheckStatement(PMLParser.CheckStatementContext ctx) {
        Expression arExpr = Expression.compile(visitorCtx, ctx.ar, Type.string());
        Expression targetExpr = Expression.compile(visitorCtx, ctx.target, Type.string(), Type.array(Type.string()));

        return new CheckStatement(arExpr, targetExpr);
    }
}
