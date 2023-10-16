package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.statement.SetNodePropertiesStatement;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.*;

class SetNodePropertiesStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.SetNodePropertiesStatementContext ctx = PMLContextVisitor.toCtx(
                "set properties of \"o1\" to {\"a\": \"b\"}",
                PMLParser.SetNodePropertiesStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        PMLStatement stmt = new SetNodePropertiesStmtVisitor(visitorCtx)
                .visitSetNodePropertiesStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new SetNodePropertiesStatement(new StringLiteral("o1"), buildMapLiteral("a", "b")),
                stmt
        );
    }

    @Test
    void testInvalidExpressions() throws PMException {
        PMLParser.SetNodePropertiesStatementContext ctx = PMLContextVisitor.toCtx(
                "set properties of [\"o1\"] to {\"a\": \"b\"}",
                PMLParser.SetNodePropertiesStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        new SetNodePropertiesStmtVisitor(visitorCtx)
                .visitSetNodePropertiesStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type string, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                "set properties of \"o1\" to [\"a\", \"b\"]",
                PMLParser.SetNodePropertiesStatementContext.class);
        visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        new SetNodePropertiesStmtVisitor(visitorCtx)
                .visitSetNodePropertiesStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type map[string]string, got []string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

}