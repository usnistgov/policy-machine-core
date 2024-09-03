package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileGlobalScope;
import gov.nist.csd.pm.pap.pml.statement.operation.DeassignStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeassignStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.DeassignStatementContext ctx = PMLContextVisitor.toCtx(
                "deassign \"a\" from [\"b\", \"c\"]",
                PMLParser.DeassignStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(new CompileGlobalScope());
        PMLStatement stmt = new DeassignStmtVisitor(visitorCtx).visitDeassignStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeassignStatement(new StringLiteral("a"), buildArrayLiteral("b", "c")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileGlobalScope());

        testCompilationError(
                "deassign \"a\" from \"c\"", visitorCtx, 1,
                "expected expression type(s) [[]string], got string"
        );

        testCompilationError(
                "deassign [\"a\"] from [\"b\", \"c\"]", visitorCtx, 1,
                "expected expression type(s) [string], got []string"
        );
    }

}