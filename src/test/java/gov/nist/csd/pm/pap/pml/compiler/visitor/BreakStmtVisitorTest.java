package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.statement.basic.BreakStatement;
import gov.nist.csd.pm.pap.pml.statement.basic.ForeachStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

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