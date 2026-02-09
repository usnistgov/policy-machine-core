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
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateProhibitionStatement;
import org.junit.jupiter.api.Test;

class CreateProhibitionStmtVisitorTest {

    @Test
    void testNodeProhibitionSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                create conj node prohibition "test"
                deny "u1"
                arset ["read"]
                include ["oa1"]
                exclude ["oa2"]
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new CreateProhibitionStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                CreateProhibitionStatement.nodeProhibition(
                        new StringLiteralExpression("test"),
                        new StringLiteralExpression("u1"),
                        buildArrayLiteral("read"),
                        buildArrayLiteral("oa1"),
                        buildArrayLiteral("oa2"),
                        true
                ),
                stmt
        );
    }

    @Test
    void testProcessProhibitionSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                create disj process prohibition "test"
                deny "u1" process "proc1"
                arset ["read"]
                include ["oa1"]
                exclude ["oa2"]
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new CreateProhibitionStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                CreateProhibitionStatement.processProhibition(
                        new StringLiteralExpression("test"),
                        new StringLiteralExpression("u1"),
                        new StringLiteralExpression("proc1"),
                        buildArrayLiteral("read"),
                        buildArrayLiteral("oa1"),
                        buildArrayLiteral("oa2"),
                        false
                ),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));

        testCompilationError(
                """
                create conj node prohibition ["test"]
                deny "u1"
                arset ["read"]
                include ["oa1"]
                exclude ["oa2"]
                """, visitorCtx, 1,
                "expected expression type string, got []string"
        );

        testCompilationError(
                """
                create conj node prohibition "test"
                deny ["u1"]
                arset ["read"]
                include ["oa1"]
                exclude ["oa2"]
                """, visitorCtx, 1,
                "expected expression type string, got []string"
        );

        testCompilationError(
                """
                create conj node prohibition "test"
                deny "u1"
                arset "read"
                include ["oa1"]
                exclude ["oa2"]
                """, visitorCtx, 1,
                "expected expression type []string, got string"
        );

        testCompilationError(
                """
                create conj node prohibition "test"
                deny "u1"
                arset ["read"]
                include "oa1"
                exclude ["oa2"]
                """, visitorCtx, 1,
                "expected expression type []string, got string"
        );

        testCompilationError(
                """
                create conj node prohibition "test"
                deny "u1"
                arset ["read"]
                include ["oa1"]
                exclude "oa2"
                """, visitorCtx, 1,
                "expected expression type []string, got string"
        );
    }

}
