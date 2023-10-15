package gov.nist.csd.pm.policy.pml.expression;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.BoolLiteral;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.BoolValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogicalExpressionTest {

    @Test
    void testCompile() {
        PMLParser.LogicalExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                true && false
                """,
                PMLParser.LogicalExpressionContext.class);
        VisitorContext visitorContext = new VisitorContext();
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
        VisitorContext visitorContext = new VisitorContext();
        Expression expression = LogicalExpression.compileLogicalExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        MemoryPolicyStore store = new MemoryPolicyStore();
        ExecutionContext executionContext = new ExecutionContext(new UserContext(""));
        Value actual = expression.execute(executionContext, store);
        assertEquals(
                new BoolValue(false),
                actual
        );

        ctx = PMLContextVisitor.toExpressionCtx(
                """
                false || true
                """,
                PMLParser.LogicalExpressionContext.class);
        visitorContext = new VisitorContext();
        expression = LogicalExpression.compileLogicalExpression(visitorContext, ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size());

        store = new MemoryPolicyStore();
        executionContext = new ExecutionContext(new UserContext(""));
        actual = expression.execute(executionContext, store);
        assertEquals(
                new BoolValue(true),
                actual
        );
    }
}