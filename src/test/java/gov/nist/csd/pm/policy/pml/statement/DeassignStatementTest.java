package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.pml.expression.NegatedExpression;
import gov.nist.csd.pm.policy.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class DeassignStatementTest {

    @Test
    void testSuccess() throws PMException {
        DeassignStatement stmt = new DeassignStatement(
                new StringLiteral("ua3"),
                buildArrayLiteral("ua1", "ua2")
        );

        MemoryPolicyStore store = new MemoryPolicyStore();
        store.graph().createPolicyClass("pc1");
        store.graph().createUserAttribute("ua1", "pc1");
        store.graph().createUserAttribute("ua2", "pc1");
        store.graph().createUserAttribute("ua3", "ua1", "ua2", "pc1");
        store.graph().createUser("u1", "ua1");

        ExecutionContext execCtx = new ExecutionContext(new UserContext("u1"));
        stmt.execute(execCtx, store);

        assertEquals(
                List.of("pc1"),
                store.graph().getParents("ua3")
        );
    }

    @Test
    void testToFormattedString() {
        DeassignStatement stmt = new DeassignStatement(
                new StringLiteral("ua3"),
                buildArrayLiteral("ua1", "ua2")
        );

        assertEquals(
                """
                        deassign "ua3" from ["ua1", "ua2"]""",
                stmt.toFormattedString(0)
        );
        assertEquals(
                """
                            deassign "ua3" from ["ua1", "ua2"]
                        """,
                stmt.toFormattedString(1) + "\n"
        );
    }

}