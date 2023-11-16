package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.function.builtin.Equals;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.statement.CreateNonPCStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.policy.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.*;

class CreateNonPCStmtVisitorTest {

    private static GlobalScope<Variable, FunctionSignature> testGlobalScope;

    @BeforeAll
    static void setup() throws PMException {
        testGlobalScope = GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore())
                                     .withPersistedFunctions(Map.of("equals", new Equals().getSignature()));
    }

    @Test
    void testSuccess() {
        PMLParser.CreateNonPCStatementContext ctx = PMLContextVisitor.toCtx(
                """
                create user attribute "ua1" with properties {"k": "v"} assign to ["a"]
                """,
                PMLParser.CreateNonPCStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
        PMLStatement stmt = new CreateNonPCStmtVisitor(visitorCtx).visitCreateNonPCStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new CreateNonPCStatement(new StringLiteral("ua1"), NodeType.UA, buildArrayLiteral("a"), buildMapLiteral("k", "v")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() {
        PMLParser.CreateNonPCStatementContext ctx = PMLContextVisitor.toCtx(
                """
                create user attribute ["ua1"] with properties {"k": "v"} assign to ["a"]
                """,
                PMLParser.CreateNonPCStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
        new CreateNonPCStmtVisitor(visitorCtx).visitCreateNonPCStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type string, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                create user attribute "ua1" with properties ["k", "v"] assign to ["a"]
                """,
                PMLParser.CreateNonPCStatementContext.class);
        visitorCtx = new VisitorContext(testGlobalScope);
        new CreateNonPCStmtVisitor(visitorCtx).visitCreateNonPCStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type map[string]string, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                create user attribute "ua1" with properties {"k": "v"} assign to "a"
                """,
                PMLParser.CreateNonPCStatementContext.class);
        visitorCtx = new VisitorContext(testGlobalScope);
        new CreateNonPCStmtVisitor(visitorCtx).visitCreateNonPCStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type []string, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

}