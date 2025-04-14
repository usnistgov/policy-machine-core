package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.statement.basic.ContinueStatement;
import gov.nist.csd.pm.pap.pml.statement.basic.ForeachStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ContinueStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                foreach x in ["a"] {
                    continue
                }
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement stmt = new ForeachStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new ForeachStatement("x", null, buildArrayLiteral("a"), List.of(
                        new ContinueStatement()
                )),
                stmt
        );
    }

    @Test
    void testNotInForLoop() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());

        testCompilationError(
                """
                continue
                """, visitorCtx, 1,
                "continue statement not in foreach"
                );
    }

}