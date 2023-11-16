package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.statement.DeleteStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteStmtVisitorTest {

    @Test
    void testDeleteNode() throws PMException {
        PMLParser.DeleteStatementContext ctx = PMLContextVisitor.toCtx(
                """
                delete object attribute "oa1"
                """,
                PMLParser.DeleteStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteStatement(DeleteStatement.Type.OBJECT_ATTRIBUTE, new StringLiteral("oa1")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        PMLParser.DeleteStatementContext ctx = PMLContextVisitor.toCtx(
                """
                delete object attribute ["oa1"]
                """,
                PMLParser.DeleteStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type string, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testDeleteObligation() throws PMException {
        PMLParser.DeleteStatementContext ctx = PMLContextVisitor.toCtx(
                """
                delete obligation "test"
                """,
                PMLParser.DeleteStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteStatement(DeleteStatement.Type.OBLIGATION, new StringLiteral("test")),
                stmt
        );
    }

    @Test
    void testDeleteProhibition() throws PMException {
        PMLParser.DeleteStatementContext ctx = PMLContextVisitor.toCtx(
                """
                delete prohibition "test"
                """,
                PMLParser.DeleteStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteStatement(DeleteStatement.Type.PROHIBITION, new StringLiteral("test")),
                stmt
        );
    }

    @Test
    void testDeleteFunction() throws PMException {
        PMLParser.DeleteStatementContext ctx = PMLContextVisitor.toCtx(
                """
                delete function "test"
                """,
                PMLParser.DeleteStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteStatement(DeleteStatement.Type.FUNCTION, new StringLiteral("test")),
                stmt
        );
    }

    @Test
    void testDeleteConstant() throws PMException {
        PMLParser.DeleteStatementContext ctx = PMLContextVisitor.toCtx(
                """
                delete const "test"
                """,
                PMLParser.DeleteStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        PMLStatement stmt = new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new DeleteStatement(DeleteStatement.Type.CONST, new StringLiteral("test")),
                stmt
        );
    }
}