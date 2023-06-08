package gov.nist.csd.pm.pap.pml.statement;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.scope.ExecuteGlobalScope;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.scope.GlobalScope;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VariableAssignmentStatementTest {

    @Test
    void testSuccess() throws PMException {
        VariableAssignmentStatement stmt = new VariableAssignmentStatement(
                "a", false, new StringLiteral("test")
        );

        ExecutionContext ctx = new ExecutionContext(new UserContext("u1"), new MemoryPAP());
        ctx.scope().addVariable("a", new StringValue("a"));
        stmt.execute(ctx, new MemoryPAP());

        assertEquals(new StringValue("test"), ctx.scope().getVariable("a"));

        stmt = new VariableAssignmentStatement(
                "a", true, new StringLiteral("test")
        );

        stmt.execute(ctx, new MemoryPAP());

        assertEquals(new StringValue("testtest"), ctx.scope().getVariable("a"));
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