package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileGlobalScope;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringLiteralTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.LiteralExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                "test"
                """,
                PMLParser.LiteralExpressionContext.class);

        VisitorContext visitorContext = new VisitorContext(new CompileGlobalScope());
        Expression expression = Literal.compileLiteral(visitorContext, ctx);
        assertTrue(expression instanceof StringLiteral);

        StringLiteral a = (StringLiteral) expression;
        assertEquals(
                new StringLiteral("test"),
                a
        );
        assertEquals(
                Type.string(),
                a.getType(new Scope<>(new CompileGlobalScope()))
        );

    }

}