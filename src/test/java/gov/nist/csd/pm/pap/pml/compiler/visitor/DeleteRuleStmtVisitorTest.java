package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.scope.CompileGlobalScope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.DeleteRuleStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteRuleStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.DeleteRuleStatementContext ctx = PMLContextVisitor.toCtx(
                """
                delete rule "rule1" from obligation "obl1"
                """,
                PMLParser.DeleteRuleStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(new CompileGlobalScope());
        PMLStatement stmt = new DeleteRuleStmtVisitor(visitorCtx).visitDeleteRuleStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteRuleStatement(new StringLiteral("rule1"), new StringLiteral("obl1")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileGlobalScope());

        testCompilationError(
                """
                delete rule ["rule1"] from obligation "obl1"
                """, visitorCtx, 1,
                "expected expression type(s) [string], got []string"
        );

        testCompilationError(
                """
                delete rule "rule1" from obligation ["obl1"]
                """, visitorCtx, 1,
                "expected expression type(s) [string], got []string"
        );
    }

}