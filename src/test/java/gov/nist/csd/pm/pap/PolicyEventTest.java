package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.VariableReference;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
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
import gov.nist.csd.pm.policy.author.pal.statement.CreatePolicyStatement;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PolicyEventTest {

    @Test
    void testEvents() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());

        List<PolicyEvent> events = new ArrayList<>();
        pap.addEventListener(events::add, false);

        pap.setResourceAccessRights(new AccessRightSet("read"));
        pap.createPolicyClass("pc1");
        pap.createObjectAttribute("oa1", "pc1");
        pap.createUserAttribute("ua1", "pc1");
        pap.createUserAttribute("ua2", "pc1");
        pap.createObject("o1", "oa1");
        pap.createUser("u1", "ua1");
        pap.createUser("u2", "ua1");
        pap.setNodeProperties("u1", Map.of("k", "v"));
        pap.deleteNode("u1");
        pap.assign("u2", "ua2");
        pap.deassign("u2", "ua2");
        pap.associate("ua1", "oa1", new AccessRightSet());
        pap.dissociate("ua1", "oa1");
        pap.createProhibition("label", ProhibitionSubject.user("ua1"), new AccessRightSet("read"), false, new ContainerCondition("oa1", false));
        pap.updateProhibition("label", ProhibitionSubject.user("ua2"), new AccessRightSet("read"), false, new ContainerCondition("oa1", false));
        pap.deleteProhibition("label");
        pap.createObligation(
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
        pap.updateObligation(new UserContext(SUPER_USER),
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
        pap.deleteObligation("label");

        System.out.println(events);

        assertEquals(24, events.size());
    }

}
