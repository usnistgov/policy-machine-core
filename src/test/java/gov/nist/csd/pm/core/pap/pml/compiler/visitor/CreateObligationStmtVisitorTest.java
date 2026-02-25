package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.AnyOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.subject.ProcessSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateObligationStatement;
import java.util.List;
import org.junit.jupiter.api.Test;

class CreateObligationStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                create obligation "test"
                when any user
                performs any operation
                do(ctx) {}
                """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new CreateObligationStatement(
                    new StringLiteralExpression("test"),
                    new EventPattern(
                        new SubjectPattern(),
                        new AnyOperationPattern()
                    ),
                    new ObligationResponse("ctx", List.of())
                ),
                stmt
        );
    }

    @Test
    void testInvalidNameExpression() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));

        testCompilationError(
                """
                create obligation ["test"] {}
                """, visitorCtx, 1,
                "expected expression type string, got []string"
                );
    }

    @Test
    void testVariableInProcess() throws PMException {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement("""
            create obligation "o1"
            when process test
            performs any operation
            do(ctx) {}
            """);
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        visitorCtx.scope().addVariable("test", new Variable("test", STRING_TYPE, false));
        PMLStatement<?> stmt = new CreateObligationStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
            new CreateObligationStatement(
                new StringLiteralExpression("o1"),
                new EventPattern(
                    new SubjectPattern(new ProcessSubjectPatternExpression(new VariableReferenceExpression<>("test", STRING_TYPE))),
                    new AnyOperationPattern()
                ),
                new ObligationResponse("ctx", List.of())
            ),
            stmt
        );
    }
}