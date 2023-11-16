package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShortDeclarationStatementTest {

    @Test
    void testSuccess() throws PMException {
        ShortDeclarationStatement stmt = new ShortDeclarationStatement(
                "a", new StringLiteral("test"));

        ExecutionContext ctx = new ExecutionContext(new UserContext("u1"), GlobalScope.withValuesAndDefinitions(new MemoryPolicyStore()));
        stmt.execute(ctx, new MemoryPolicyStore());

        assertEquals(new StringValue("test"), ctx.scope().getVariable("a"));
    }

    @Test
    void testToFormattedString() {
        ShortDeclarationStatement stmt = new ShortDeclarationStatement(
                "a", new StringLiteral("test"));

        assertEquals(
                "a := \"test\"",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    a := \"test\"",
                stmt.toFormattedString(1)
        );
    }
}