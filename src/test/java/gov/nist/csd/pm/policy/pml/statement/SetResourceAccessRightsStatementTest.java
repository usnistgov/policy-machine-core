package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.policy.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.*;

class SetResourceAccessRightsStatementTest {

    @Test
    void testSuccess() throws PMException {
        SetResourceAccessRightsStatement stmt = new SetResourceAccessRightsStatement(
                buildArrayLiteral("a", "b", "c", "d")
        );

        MemoryPolicyStore store = new MemoryPolicyStore();

        stmt.execute(new ExecutionContext(new UserContext("")), store);

        assertEquals(
                new AccessRightSet("a", "b", "c", "d"),
                store.graph().getResourceAccessRights()
        );
    }

    @Test
    void testToFormattedString() {
        SetResourceAccessRightsStatement stmt = new SetResourceAccessRightsStatement(
                buildArrayLiteral("a", "b", "c", "d")
        );

        assertEquals(
                "set resource access rights [\"a\", \"b\", \"c\", \"d\"]",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    set resource access rights [\"a\", \"b\", \"c\", \"d\"]",
                stmt.toFormattedString(1)
        );
    }

}