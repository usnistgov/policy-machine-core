package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateNonPCStatement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
                create UA "ua1" in ["a"]
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
                create UA ["ua1"] in ["a"]
                """, visitorCtx, 1,
                "expected expression type string, got []string"
        );

        testCompilationError(
                """
                create UA "ua1" in "a"
                """, visitorCtx, 1,
                "expected expression type []string, got string"
        );
    }

}