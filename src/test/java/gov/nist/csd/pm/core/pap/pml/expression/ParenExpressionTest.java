package gov.nist.csd.pm.core.pap.pml.expression;


import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.BOOLEAN_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ParenExpressionTest {

    private static CompileScope compileScope;

    @BeforeAll
    static void setup() throws PMException {
        compileScope = new CompileScope();
    }

    @Test
    void testParenExpression() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                true && (true || false)
                """);
        VisitorContext visitorContext = new VisitorContext(compileScope);
        Expression e = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        Object actual = e.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                true,
                actual
        );

        ctx = TestPMLParser.parseExpression(
                """
                (false || false) && (true || false)
                """);
        visitorContext = new VisitorContext(compileScope);
        e = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        actual = e.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                false,
                actual
        );

        ctx = TestPMLParser.parseExpression(
                """
                (false || false) || (true || false)
                """);
        visitorContext = new VisitorContext(compileScope);
        e = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        actual = e.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                true,
                actual
        );

        ctx = TestPMLParser.parseExpression(
                """
                !(false || false) && (true || false)
                """);
        visitorContext = new VisitorContext(compileScope);
        e = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        actual = e.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                true,
                actual
        );

        ctx = TestPMLParser.parseExpression(
                """
                !(false || false) && (false || false || true)
                """);
        visitorContext = new VisitorContext(compileScope);
        e = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        actual = e.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                true,
                actual
        );

        ctx = TestPMLParser.parseExpression(
                """
                !(false || false) && (false || false || true) && false
                """);
        visitorContext = new VisitorContext(compileScope);
        e = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());
        actual = e.execute(new ExecutionContext(new UserContext(0), new MemoryPAP()), new MemoryPAP());
        assertEquals(
                false,
                actual
        );
    }

    @Test
    void testComplexParen() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                ((true || (true && false)) && (false || (false && true)))
                """);
        VisitorContext visitorContext = new VisitorContext(compileScope);
        Expression expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        PAP pap = new TestPAP();
        ExecutionContext executionContext = new ExecutionContext(new UserContext(0), new MemoryPAP());
        Object actual = expression.execute(executionContext, pap);
        assertEquals(
                false,
                actual
        );
    }

}