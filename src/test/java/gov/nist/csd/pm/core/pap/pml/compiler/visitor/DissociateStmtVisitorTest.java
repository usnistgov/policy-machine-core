package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

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
import gov.nist.csd.pm.core.pap.pml.statement.operation.DissociateStatement;
import org.junit.jupiter.api.Test;

class DissociateStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                dissociate "a" and "b"
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new DissociateStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DissociateStatement(new StringLiteralExpression("a"), new StringLiteralExpression("b")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));

        testCompilationError(
                """
                dissociate ["a"] and "b"
                """, visitorCtx, 1,
                "expected expression type string, got []string"
                );

        testCompilationError(
                """
                dissociate "a" and ["b"]
                """, visitorCtx, 1,
                "expected expression type string, got []string"
        );
    }

}