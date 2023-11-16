package gov.nist.csd.pm.policy.pml.expression.literal;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.scope.Scope;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoolLiteralTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.LiteralExpressionContext ctx = PMLContextVisitor.toExpressionCtx(
                """
                true
                """,
                PMLParser.LiteralExpressionContext.class);

        GlobalScope<Variable, FunctionSignature> globalScope = GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore());

        VisitorContext visitorContext = new VisitorContext(globalScope);
        Expression expression = Literal.compileLiteral(visitorContext, ctx);
        assertTrue(expression instanceof BoolLiteral);

        BoolLiteral a = (BoolLiteral) expression;
        assertEquals(
               new BoolLiteral(true),
                a
        );
        assertEquals(
                Type.bool(),
                a.getType(new Scope<>(globalScope))
        );

    }

}