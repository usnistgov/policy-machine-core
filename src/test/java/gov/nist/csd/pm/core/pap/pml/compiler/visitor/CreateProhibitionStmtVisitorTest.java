package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubjectType;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.MapLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateProhibitionStatement;

import java.util.Map;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateProhibitionStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                create prohibition "test"
                deny U "u1"
                access rights ["read"]
                on union of {"oa1": true}
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope());
        PMLStatement<?> stmt = new CreateProhibitionStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new CreateProhibitionStatement(
                        new StringLiteralExpression("test"),
                        new StringLiteralExpression("u1"),
                        ProhibitionSubjectType.USER,
                        buildArrayLiteral("read"),
                        false,
                        MapLiteralExpression.of(Map.of(
                            new StringLiteralExpression("oa1"), new BoolLiteralExpression(true)
                        ), STRING_TYPE, BOOLEAN_TYPE)
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
                deny U "u1"
                access rights ["read"]
                on union of {"oa1": true}
                """, visitorCtx, 1,
                "expected expression type string, got []string"
        );

        testCompilationError(
                """
                create prohibition "test"
                deny U ["u1"]
                access rights ["read"]
                on union of {"oa1": true}
                """, visitorCtx, 1,
                "expected expression type string, got []string"
                );

        testCompilationError(
                """
                create prohibition "test"
                deny U "u1"
                access rights "read"
                on union of {"oa1": true}
                """, visitorCtx, 1,
                "expected expression type []string, got string"
                );

        testCompilationError(
                """
                create prohibition "test"
                deny U "u1"
                access rights ["read"]
                on union of "oa1"
                """, visitorCtx, 1,
                "expected expression type map[string]bool, got string"
                );

    }

}