package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.basic.BreakStatement;
import gov.nist.csd.pm.core.pap.pml.statement.basic.ForeachStatement;
import java.util.List;
import org.junit.jupiter.api.Test;

class BreakStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                foreach x in ["a"] {
                    break
                }
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement<?> stmt = new ForeachStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        ForeachStatement expected = new ForeachStatement("x", null, buildArrayLiteral("a"), List.of(
            new BreakStatement()
        ));
        assertEquals(
                expected.toString(),
                stmt.toString()
        );
    }

    @Test
    void testNotInForLoop() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());

        testCompilationError(
                """
                break
                """, visitorCtx, 1,
                "break statement not in foreach"
        );
    }

}