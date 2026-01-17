package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ExpressionContext;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import org.junit.jupiter.api.Test;

class ExpressionVisitorTest {

    @Test
    void testCompileWithObjectAsExpectedReturnType() throws PMException {
        ExpressionContext ctx = TestPMLParser.parseExpression("""
                "test"
                """);
        Expression<Object> actual = ExpressionVisitor.compile(new VisitorContext(new CompileScope(new MemoryPAP())), ctx, ANY_TYPE);
        assertEquals(STRING_TYPE, actual.getType());
        assertEquals(StringLiteralExpression.class, actual.getClass());
    }

    @Test
    void testCompileWithNoDefinedExpectedType() throws PMException {
        ExpressionContext ctx = TestPMLParser.parseExpression("""
                "test"
                """);
        Expression<?> actual = ExpressionVisitor.compile(new VisitorContext(new CompileScope(new MemoryPAP())), ctx);
        assertEquals(STRING_TYPE, actual.getType());
        assertEquals(StringLiteralExpression.class, actual.getClass());
    }

}