package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateNonPCStatement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateNonPCStmtVisitorTest {

    private static CompileScope testGlobalScope;

    @BeforeAll
    static void setup() throws PMException {
        testGlobalScope = new CompileScope();
    }

    @Test
    void testSuccess() {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                create user attribute "ua1" in ["a"]
                """);
        VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
        PMLStatement stmt = new CreateNonPCStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new CreateNonPCStatement(new StringLiteralExpression("ua1"), NodeType.UA, buildArrayLiteral("a")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() {
        VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
        testCompilationError(
                """
                create user attribute ["ua1"] in ["a"]
                """, visitorCtx, 1,
                "expected expression type string, got []string"
        );

        testCompilationError(
                """
                create user attribute "ua1" in "a"
                """, visitorCtx, 1,
                "expected expression type []string, got string"
        );
    }

}