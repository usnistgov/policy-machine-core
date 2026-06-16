package gov.nist.csd.pm.core.pap.pml.statement;


import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.statement.basic.ShortDeclarationStatement;
import org.junit.jupiter.api.Test;
import gov.nist.csd.pm.core.pap.query.model.context.NodeUserContext;

class ShortDeclarationStatementTest {

    @Test
    void testSuccess() throws PMException {
        ShortDeclarationStatement stmt = new ShortDeclarationStatement(
                "a", new StringLiteralExpression("test"));

        ExecutionContext ctx = new ExecutionContext(NodeUserContext.of(0), new MemoryPAP());
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