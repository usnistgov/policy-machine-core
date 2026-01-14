package gov.nist.csd.pm.core.pap.pml.statement;


import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.statement.basic.VariableAssignmentStatement;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import org.junit.jupiter.api.Test;

class VariableAssignmentStatementTest {

    @Test
    void testSuccess() throws PMException {
        VariableAssignmentStatement stmt = new VariableAssignmentStatement(
                "a", false, new StringLiteralExpression("test")
        );

        ExecutionContext ctx = new ExecutionContext(new UserContext(0), new MemoryPAP());
        ctx.scope().addVariable("a", "a");
        stmt.execute(ctx, new MemoryPAP());

        assertEquals("test", ctx.scope().getVariable("a"));

        stmt = new VariableAssignmentStatement(
                "a", true, new StringLiteralExpression("test")
        );

        stmt.execute(ctx, new MemoryPAP());

        assertEquals("testtest", ctx.scope().getVariable("a"));
    }

    @Test
    void testToFormattedString() {
        VariableAssignmentStatement stmt = new VariableAssignmentStatement(
                "a", true, new StringLiteralExpression("test")
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
                "a", false, new StringLiteralExpression("test")
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