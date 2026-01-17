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
import gov.nist.csd.pm.core.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;

class LogicalExpressionTest {

    @Test
    void testCompile() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                true && false
                """);
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression<Boolean> expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        LogicalExpression logicalExpression = (LogicalExpression) expression;
        assertEquals(
                new LogicalExpression(new BoolLiteralExpression(true), new BoolLiteralExpression(false), true),
                logicalExpression
        );
    }

    @Test
    void testExecute() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                true && false
                """);
        PAP pap = new TestPAP();

        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression<Boolean> expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        ExecutionContext executionContext = new ExecutionContext(new UserContext(0), pap);
        Object actual = expression.execute(executionContext, pap);
        assertEquals(
                false,
                actual
        );

        ctx = TestPMLParser.parseExpression(
                """
                false || true
                """);
        visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        expression = ExpressionVisitor.compile(visitorContext, ctx, BOOLEAN_TYPE);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        pap = new TestPAP();
        executionContext = new ExecutionContext(new UserContext(0), pap);
        actual = expression.execute(executionContext, pap);
        assertEquals(
                true,
                actual
        );
    }
}