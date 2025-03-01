package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.literal.BoolLiteral;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogicalExpressionTest {

    @Test
    void testCompile() throws PMException {
        PMLParser.LogicalExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                true && false
                """,
                PMLParser.LogicalExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression expression = LogicalExpression.compileLogicalExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        LogicalExpression logicalExpression = (LogicalExpression) expression;
        assertEquals(
                new LogicalExpression(new BoolLiteral(true), new BoolLiteral(false), true),
                logicalExpression
        );
    }

    @Test
    void testExecute() throws PMException {
        PMLParser.LogicalExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                true && false
                """,
                PMLParser.LogicalExpressionContext.class);
        PAP pap = new TestPAP();

        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        Expression expression = LogicalExpression.compileLogicalExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        ExecutionContext executionContext = new ExecutionContext(new UserContext(0), pap);
        Value actual = expression.execute(executionContext, pap);
        assertEquals(
                new BoolValue(false),
                actual
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                false || true
                """,
                PMLParser.LogicalExpressionContext.class);
        visitorContext = new VisitorContext(new CompileScope());
        expression = LogicalExpression.compileLogicalExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        pap = new TestPAP();
        executionContext = new ExecutionContext(new UserContext(0), pap);
        actual = expression.execute(executionContext, pap);
        assertEquals(
                new BoolValue(true),
                actual
        );
    }
}