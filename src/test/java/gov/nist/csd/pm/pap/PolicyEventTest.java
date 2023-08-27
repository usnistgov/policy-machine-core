package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.VariableReference;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.SuperUserBootstrapper.SUPER_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PolicyEventTest {

    @Test
    void testEvents() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.bootstrap(new SuperUserBootstrapper());

        List<PolicyEvent> events = new ArrayList<>();
        pap.addEventListener(events::add, false);

        pap.graph().setResourceAccessRights(new AccessRightSet("read"));
        assertEquals(1, events.size());

        pap.graph().createPolicyClass("pc1");
        assertEquals(3, events.size());

        pap.graph().createObjectAttribute("oa1", "pc1");
        assertEquals(4, events.size());

        pap.graph().createUserAttribute("ua1", "pc1");
        assertEquals(5, events.size());

        pap.graph().createUserAttribute("ua2", "pc1");
        assertEquals(6, events.size());

        pap.graph().createObject("o1", "oa1");
        assertEquals(7, events.size());

        pap.graph().createUser("u1", "ua1");
        assertEquals(8, events.size());

        pap.graph().createUser("u2", "ua1");
        assertEquals(9, events.size());

        pap.graph().setNodeProperties("u1", Map.of("k", "v"));
        assertEquals(10, events.size());

        pap.graph().deleteNode("u1");
        assertEquals(11, events.size());

        pap.graph().assign("u2", "ua2");
        assertEquals(12, events.size());

        pap.graph().deassign("u2", "ua2");
        assertEquals(13, events.size());

        pap.graph().associate("ua1", "oa1", new AccessRightSet());
        assertEquals(14, events.size());

        pap.graph().dissociate("ua1", "oa1");
        assertEquals(15, events.size());

        pap.prohibitions().create("label", ProhibitionSubject.user("ua1"), new AccessRightSet("read"), false, new ContainerCondition("oa1", false));
        assertEquals(16, events.size());

        pap.prohibitions().update("label", ProhibitionSubject.user("ua2"), new AccessRightSet("read"), false, new ContainerCondition("oa1", false));
        assertEquals(17, events.size());

        pap.prohibitions().delete("label");
        assertEquals(18, events.size());

        pap.obligations().create(
                new UserContext(SUPER_USER),
                "label",
                new Rule(
                        "rule1",
                        new EventPattern(
                                EventSubject.anyUser(),
                                new Performs("test_event")
                        ),
                        new Response(
                                new UserContext(SUPER_USER),
                                new CreatePolicyStatement(new Expression(new VariableReference("test_pc", Type.string())))
                        )
                )
        );
        assertEquals(19, events.size());

        pap.obligations().update(new UserContext(SUPER_USER),
                "label",
                new Rule(
                        "rule1",
                        new EventPattern(
                                EventSubject.anyUser(),
                                new Performs("test_event")
                        ),
                        new Response(
                                new UserContext(SUPER_USER),
                                new CreatePolicyStatement(new Expression(new VariableReference("test_pc2", Type.string())))
                        )
                ));
        assertEquals(20, events.size());

        pap.obligations().delete("label");
        assertEquals(21, events.size());
    }

}
