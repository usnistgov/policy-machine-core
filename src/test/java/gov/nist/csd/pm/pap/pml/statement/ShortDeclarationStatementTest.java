package gov.nist.csd.pm.pap.pml.statement;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.statement.basic.ShortDeclarationStatement;

import gov.nist.csd.pm.pap.query.model.context.UserContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShortDeclarationStatementTest {

    @Test
    void testSuccess() throws PMException {
        ShortDeclarationStatement stmt = new ShortDeclarationStatement(
                "a", new StringLiteralExpression("test"));

        ExecutionContext ctx = new ExecutionContext(new UserContext(0), new MemoryPAP());
        stmt.execute(ctx, new MemoryPAP());

        assertEquals("test", ctx.scope().getVariable("a"));
    }

    @Test
    void testToFormattedString() {
        ShortDeclarationStatement stmt = new ShortDeclarationStatement(
                "a", new StringLiteralExpression("test"));

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