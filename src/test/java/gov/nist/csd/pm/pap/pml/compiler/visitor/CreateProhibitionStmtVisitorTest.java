package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubjectType;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.NegatedExpression;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateProhibitionStatement;

import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateProhibitionStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.CreateProhibitionStatementContext ctx = PMLContextVisitor.toCtx(
                """
                create prohibition "test"
                deny user "u1"
                ["read"]
                on union of [!"oa1"]
                """,
                PMLParser.CreateProhibitionStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement stmt = new CreateProhibitionStmtVisitor(visitorCtx).visitCreateProhibitionStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new CreateProhibitionStatement(
                        new StringLiteral("test"),
                        new StringLiteral("u1"),
                        ProhibitionSubjectType.USER,
                        buildArrayLiteral("read"),
                        false,
                        new ArrayLiteral(List.of(new NegatedExpression(new StringLiteral("oa1"))), STRING_TYPE)
                ),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());

        testCompilationError(
                """
                create prohibition ["test"]
                deny user "u1"
                ["read"]
                on union of [!"oa1"]
                """, visitorCtx, 1,
                "expected expression type(s) [string], got []string"
        );

        testCompilationError(
                """
                create prohibition "test"
                deny user ["u1"]
                ["read"]
                on union of [!"oa1"]
                """, visitorCtx, 1,
                "expected expression type(s) [string], got []string"
                );

        testCompilationError(
                """
                create prohibition "test"
                deny user "u1"
                "read"
                on union of [!"oa1"]
                """, visitorCtx, 1,
                "expected expression type(s) [[]string], got string"
                );

        testCompilationError(
                """
                 create prohibition "test"
                deny user "u1"
                ["read"]
                on union of !"oa1"
                """, visitorCtx, 1,
                "expected expression type(s) [[]string], got string"
                );

    }

}