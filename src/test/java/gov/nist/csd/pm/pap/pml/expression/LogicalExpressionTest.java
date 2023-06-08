package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.scope.CompileGlobalScope;
import gov.nist.csd.pm.pap.pml.scope.ExecuteGlobalScope;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.literal.BoolLiteral;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.GlobalScope;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
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
        VisitorContext visitorContext = new VisitorContext(new CompileGlobalScope());
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
        PAP pap = new MemoryPAP();

        VisitorContext visitorContext = new VisitorContext(new CompileGlobalScope());
        Expression expression = LogicalExpression.compileLogicalExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        ExecutionContext executionContext = new ExecutionContext(new UserContext(""), pap);
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
        visitorContext = new VisitorContext(new CompileGlobalScope());
        expression = LogicalExpression.compileLogicalExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        pap = new MemoryPAP();
        executionContext = new ExecutionContext(new UserContext(""), pap);
        actual = expression.execute(executionContext, pap);
        assertEquals(
                new BoolValue(true),
                actual
        );
    }
}