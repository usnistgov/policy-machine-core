package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static gov.nist.csd.pm.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreatePolicyStatementTest {

    @Test
    void testSuccess() throws PMException {
        CreatePolicyStatement stmt = new CreatePolicyStatement(new StringLiteral("pc1"));
        MemoryPAP pap = new TestPAP();
        pap.modify().graph().createPolicyClass("pc2");
        pap.modify().graph().createUserAttribute("ua2", ids("pc2"));
        pap.modify().graph().createUser("u2", ids("ua2"));
        ExecutionContext execCtx = new ExecutionContext(new UserContext(id("u2")), pap);

        stmt.execute(execCtx, pap);

        assertTrue(pap.query().graph().nodeExists("pc1"));
    }

    @Test
    void testToFormattedString() {
        CreatePolicyStatement s = new CreatePolicyStatement(
                new StringLiteral("pc1")
        );
        assertEquals(
                "create PC \"pc1\"",
                s.toFormattedString(0)
        );
        assertEquals(
                "    create PC \"pc1\"",
                s.toFormattedString(1)
        );

        s = new CreatePolicyStatement(new StringLiteral("a"));
        assertEquals("create PC \"a\"", s.toFormattedString(0));

        s = new CreatePolicyStatement(new StringLiteral("a"));
        assertEquals("create PC \"a\"", s.toFormattedString(0));

        s = new CreatePolicyStatement(new StringLiteral("a"));
        assertEquals("    create PC \"a\"", s.toFormattedString(1));

        s = new CreatePolicyStatement(new StringLiteral("a"));
        assertEquals("    create PC \"a\"", s.toFormattedString(1));
    }
}