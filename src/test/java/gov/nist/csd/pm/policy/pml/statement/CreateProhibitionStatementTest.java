package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.pml.expression.NegatedExpression;
import gov.nist.csd.pm.policy.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class CreateProhibitionStatementTest {

    @Test
    void testSuccess() throws PMException {
        CreateProhibitionStatement stmt = new CreateProhibitionStatement(
                new StringLiteral("pro1"),
                new StringLiteral("ua2"),
                ProhibitionSubject.Type.USER_ATTRIBUTE,
                buildArrayLiteral("read"),
                true,
                new ArrayLiteral(
                        Type.string(), new StringLiteral("oa1"), new NegatedExpression(new StringLiteral("oa2")))
        );

        MemoryPolicyStore store = new MemoryPolicyStore();
        store.graph().setResourceAccessRights(new AccessRightSet("read"));
        store.graph().createPolicyClass("pc2");
        store.graph().createUserAttribute("ua2", "pc2");
        store.graph().createUser("u2", "ua2");
        store.graph().createObjectAttribute("oa1", "pc2");
        store.graph().createObjectAttribute("oa2", "pc2");

        ExecutionContext execCtx = new ExecutionContext(new UserContext("u2"), GlobalScope.withValuesAndDefinitions(new MemoryPolicyStore()));

        stmt.execute(execCtx, store);

        assertTrue(store.prohibitions().exists("pro1"));

        Prohibition prohibition = store.prohibitions().get("pro1");
        assertEquals(
                new ProhibitionSubject("ua2", ProhibitionSubject.Type.USER_ATTRIBUTE),
                prohibition.getSubject()
        );
        assertTrue(prohibition.isIntersection());
        assertEquals(
                new AccessRightSet("read"),
                prohibition.getAccessRightSet()
        );
        assertEquals(
                List.of(new ContainerCondition("oa1", false), new ContainerCondition("oa2", true)),
                prohibition.getContainers()
        );
    }

    @Test
    void testToFormattedString() {
        CreateProhibitionStatement stmt = new CreateProhibitionStatement(
                new StringLiteral("pro1"),
                new StringLiteral("ua2"),
                ProhibitionSubject.Type.USER_ATTRIBUTE,
                buildArrayLiteral("read"),
                true,
                new ArrayLiteral(
                        Type.string(), new StringLiteral("oa1"), new NegatedExpression(new StringLiteral("oa2")))
        );
        assertEquals(
                """
                        create prohibition "pro1"
                          deny UA "ua2"
                          access rights ["read"]
                          on intersection of ["oa1", !"oa2"]""",
                stmt.toFormattedString(0)
        );
        assertEquals(
                """
                            create prohibition "pro1"
                              deny UA "ua2"
                              access rights ["read"]
                              on intersection of ["oa1", !"oa2"]""",
                stmt.toFormattedString(0)
        );
    }
}