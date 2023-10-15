package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.CreateObligationStatement;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateObligationStmtVisitorTest {

    @Test
    void testSuccess() {
        PMLParser.CreateObligationStatementContext ctx = PMLContextVisitor.toCtx(
                """
                create obligation "test" {}
                """,
                PMLParser.CreateObligationStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        PMLStatement stmt = new CreateObligationStmtVisitor(visitorCtx).visitCreateObligationStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new CreateObligationStatement(new StringLiteral("test"), List.of()),
                stmt
        );
    }

    @Test
    void testInvalidNameExpression() {
        PMLParser.CreateObligationStatementContext ctx = PMLContextVisitor.toCtx(
                """
                create obligation ["test"] {}
                """,
                PMLParser.CreateObligationStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        new CreateObligationStmtVisitor(visitorCtx).visitCreateObligationStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type string, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }


}