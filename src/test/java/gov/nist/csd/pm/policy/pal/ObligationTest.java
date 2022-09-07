package gov.nist.csd.pm.policy.pal;

import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.policy.author.pal.PALExecutor;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.policies.SuperPolicy.SUPER_USER;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObligationTest {

    @Test
    void testObligation() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        pap.graph().createPolicyClass("pc1", noprops());
        pap.graph().createObjectAttribute("oa1", noprops(), "pc1");

        String input = """
                create obligation 'obligation1' {
                    create rule 'rule1'
                    when any user
                    performs 'test_event'
                    on 'oa1'
                    do(evtCtx) {
                        create policy class evtCtx['eventName'];
                        
                        delete rule 'rule1' from obligation 'obligation1';
                    }
                }
                """;
        new PALExecutor(pap)
                .execute(new UserContext(SUPER_USER), input);
        Obligation obligation1 = pap.obligations().get("obligation1");
        assertEquals("obligation1", obligation1.getLabel());
        assertEquals(1, obligation1.getRules().size());
        assertEquals(new UserContext(SUPER_USER), obligation1.getAuthor());

        Rule rule = obligation1.getRules().get(0);
        assertEquals("rule1", rule.getLabel());
        assertEquals(new EventPattern(
                EventSubject.anyUser(),
                Performs.events("test_event"),
                Target.policyElement("oa1")
        ), rule.getEvent());
        assertEquals(2, rule.getResponse().getStatements().size());
    }

}
