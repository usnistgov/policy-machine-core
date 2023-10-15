package gov.nist.csd.pm.policy.pml.expression.literal;

import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.Scope;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringLiteralTest {

    @Test
    void testSuccess() throws PMLScopeException {
        PMLParser.LiteralExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                "test"
                """,
                PMLParser.LiteralExpressionContext.class);

        VisitorContext visitorContext = new VisitorContext();
        Expression expression = Literal.compileLiteral(visitorContext, ctx);
        assertTrue(expression instanceof StringLiteral);

        StringLiteral a = (StringLiteral) expression;
        assertEquals(
                new StringLiteral("test"),
                a
        );
        assertEquals(
                Type.string(),
                a.getType(new Scope(Scope.Mode.COMPILE))
        );

    }

}