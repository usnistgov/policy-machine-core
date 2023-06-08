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

class ShortDeclarationStatementTest {

    @Test
    void testSuccess() throws PMException {
        ShortDeclarationStatement stmt = new ShortDeclarationStatement(
                "a", new StringLiteral("test"));

        ExecutionContext ctx = new ExecutionContext(new UserContext("u1"), new MemoryPAP());
        stmt.execute(ctx, new MemoryPAP());

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