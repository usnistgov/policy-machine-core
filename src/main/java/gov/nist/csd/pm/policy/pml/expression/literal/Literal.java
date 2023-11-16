package gov.nist.csd.pm.policy.pml.expression.literal;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;

public abstract class Literal extends Expression {
    public static Expression compileLiteral(VisitorContext visitorCtx, PMLParser.LiteralExpressionContext literalExpressionContext) {
        return new LiteralVisitor(visitorCtx)
                .visitLiteralExpression(literalExpressionContext);
    }
}
