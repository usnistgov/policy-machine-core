package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class AssignStatementTest {

    @Test
    void testSuccess() throws PMException {
        AssignStatement stmt = new AssignStatement(new StringLiteral("u1"), buildArrayLiteral("ua2", "ua3"));

        MemoryPolicyStore store = new MemoryPolicyStore();
        store.graph().createPolicyClass("pc1");
        store.graph().createUserAttribute("ua1", "pc1");
        store.graph().createUserAttribute("ua2", "pc1");
        store.graph().createUserAttribute("ua3", "pc1");
        store.graph().createUser("u1", "ua1");
        ExecutionContext execCtx = new ExecutionContext(new UserContext("u1"));
        stmt.execute(execCtx, store);

        assertTrue(store.graph().getParents("u1").containsAll(List.of("ua1", "ua2", "ua3")));
    }

    @Test
    void testToFormattedString() {
        AssignStatement stmt = new AssignStatement(new StringLiteral("u1"), buildArrayLiteral("ua2", "ua3"));
        assertEquals(
                "assign \"u1\" to [\"ua2\", \"ua3\"]",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    assign \"u1\" to [\"ua2\", \"ua3\"]",
                stmt.toFormattedString(1)
        );
    }

}