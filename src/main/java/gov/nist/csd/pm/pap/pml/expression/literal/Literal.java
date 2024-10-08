package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;

public abstract class Literal extends Expression {

    public static Expression compileLiteral(VisitorContext visitorCtx, PMLParser.LiteralExpressionContext literalExpressionContext) {
        return new LiteralVisitor(visitorCtx)
                .visitLiteralExpression(literalExpressionContext);
    }
}
