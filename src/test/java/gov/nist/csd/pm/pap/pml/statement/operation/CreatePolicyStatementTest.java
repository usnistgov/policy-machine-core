package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.query.UserContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreatePolicyStatementTest {

    @Test
    void testSuccess() throws PMException {
        CreatePolicyStatement stmt = new CreatePolicyStatement(new StringLiteral("pc1"));
        MemoryPAP pap = new MemoryPAP();
        pap.modify().graph().createPolicyClass("pc2");
        pap.modify().graph().createUserAttribute("ua2", List.of("pc2"));
        pap.modify().graph().createUser("u2", List.of("ua2"));
        ExecutionContext execCtx = new ExecutionContext(new UserContext("u2"), pap);

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