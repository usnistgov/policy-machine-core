package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.DeassignStatement;
import org.junit.jupiter.api.Test;

class DeassignStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                deassign "a" from ["b", "c"]
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement stmt = new DeassignStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeassignStatement(new StringLiteralExpression("a"), buildArrayLiteral("b", "c")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));

        testCompilationError(
                """
                deassign "a" from "c"
                """, visitorCtx, 1,
                "expected expression type []string, got string"
        );

        testCompilationError(
                """
                deassign ["a"] from ["b", "c"]
                """, visitorCtx, 1,
                "expected expression type string, got []string"
        );
    }

}