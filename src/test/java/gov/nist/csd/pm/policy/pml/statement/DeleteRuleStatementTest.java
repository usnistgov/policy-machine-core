package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.obligation.event.subject.AnyUserSubject;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class DeleteRuleStatementTest {

    @Test
    void testSuccess() throws PMException {
        DeleteRuleStatement stmt = new DeleteRuleStatement(
                new StringLiteral("rule1"), new StringLiteral("obl1"));

        MemoryPolicyStore store = new MemoryPolicyStore();
        store.graph().createPolicyClass("pc1");
        store.graph().createUserAttribute("ua1", "pc1");
        store.graph().createUser("u1", "ua1");
        UserContext userContext = new UserContext("u1");
        store.obligations().create(userContext, "obl1", new Rule(
                "rule1",
                new EventPattern(new AnyUserSubject(), new Performs("e1")),
                new Response(userContext)
        ));

        ExecutionContext execCtx = new ExecutionContext(userContext);
        stmt.execute(execCtx, store);

        assertTrue(store.obligations().get("obl1").getRules().isEmpty());
    }

    @Test
    void testToFormattedString() {
        DeleteRuleStatement stmt = new DeleteRuleStatement(
                new StringLiteral("rule1"), new StringLiteral("obl1"));

        assertEquals(
                """
                        delete rule "rule1" from obligation "obl1"
                        """,
                stmt.toFormattedString(0) + "\n"
        );
        assertEquals(
                """
                            delete rule "rule1" from obligation "obl1"
                        """,
                stmt.toFormattedString(1) + "\n"
        );
    }

}