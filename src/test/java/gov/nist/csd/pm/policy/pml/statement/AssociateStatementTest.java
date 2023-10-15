package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class AssociateStatementTest {

    @Test
    void testSuccess() throws PMException {
        AssociateStatement stmt = new AssociateStatement(
                new StringLiteral("ua1"),
                new StringLiteral("oa1"),
                buildArrayLiteral("read")
        );

        MemoryPolicyStore store = new MemoryPolicyStore();
        store.graph().setResourceAccessRights(new AccessRightSet("read"));
        store.graph().createPolicyClass("pc1");
        store.graph().createUserAttribute("ua1", "pc1");
        store.graph().createUserAttribute("u1", "pc1");
        store.graph().createObjectAttribute("oa1", "pc1");
        ExecutionContext execCtx = new ExecutionContext(new UserContext("u1"));
        stmt.execute(execCtx, store);

        assertTrue(store.graph().getAssociationsWithSource("ua1").contains(new Association("ua1", "oa1")));
        assertTrue(store.graph().getAssociationsWithTarget("oa1").contains(new Association("ua1", "oa1")));
    }

    @Test
    void testToFormattedString() {
        AssociateStatement stmt = new AssociateStatement(
                new StringLiteral("ua1"),
                new StringLiteral("oa1"),
                buildArrayLiteral("read")
        );
        assertEquals(
                "associate \"ua1\" and \"oa1\" with [\"read\"]",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    associate \"ua1\" and \"oa1\" with [\"read\"]",
                stmt.toFormattedString(1)
        );
    }

}