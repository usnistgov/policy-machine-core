package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VariableAssignmentStatementTest {

    @Test
    void testSuccess() throws PMException {
        VariableAssignmentStatement stmt = new VariableAssignmentStatement(
                "a", false, new StringLiteral("test")
        );

        ExecutionContext ctx = new ExecutionContext(new UserContext("u1"));
        ctx.scope().addValue("a", new StringValue("a"));
        stmt.execute(ctx, new MemoryPolicyStore());

        assertEquals(new StringValue("test"), ctx.scope().getValue("a"));

        stmt = new VariableAssignmentStatement(
                "a", true, new StringLiteral("test")
        );

        stmt.execute(ctx, new MemoryPolicyStore());

        assertEquals(new StringValue("testtest"), ctx.scope().getValue("a"));
    }

    @Test
    void testToFormattedString() {
        VariableAssignmentStatement stmt = new VariableAssignmentStatement(
                "a", true, new StringLiteral("test")
        );

        assertEquals(
                "a += \"test\"",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    a += \"test\"",
                stmt.toFormattedString(1)
        );

        stmt = new VariableAssignmentStatement(
                "a", false, new StringLiteral("test")
        );

        assertEquals(
                "a = \"test\"",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    a = \"test\"",
                stmt.toFormattedString(1)
        );
    }

}