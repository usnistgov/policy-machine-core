package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.AssociateStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AssociateStmtVisitorTest {

    VisitorContext visitorCtx = new VisitorContext(new CompileScope());

    AssociateStmtVisitorTest() throws PMException {
    }

    @Test
    void testSuccess() throws PMException {
        PMLParser.AssociateStatementContext ctx = PMLContextVisitor.toCtx(
                """
                associate "a" and "b" with ["c", "d"]
                """,
                PMLParser.AssociateStatementContext.class);
        PMLStatement stmt = new AssociateStmtVisitor(visitorCtx).visitAssociateStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new AssociateStatement(new StringLiteral("a"), new StringLiteral("b"), buildArrayLiteral("c", "d")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        testCompilationError(
                """
                associate ["a"] and "b" with ["c", "d"]
                """, visitorCtx, 1,
                "expected expression type(s) [string], got []string"
        );

        testCompilationError(
                """
                associate "a" and ["b"] with ["c", "d"]
                """, visitorCtx, 1,
                "expected expression type(s) [string], got []string"
        );

        testCompilationError(
                """
                associate "a" and "b" with "c"
                """, visitorCtx, 1,
                "expected expression type(s) [[]string], got string"
        );
    }

}