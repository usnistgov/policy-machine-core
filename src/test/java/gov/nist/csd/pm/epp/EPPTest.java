package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.pdp.reviewer.MemoryPolicyReviewer;
import gov.nist.csd.pm.policy.author.pal.PALExecutor;
import gov.nist.csd.pm.policy.author.pal.model.expression.Literal;
import gov.nist.csd.pm.policy.author.pal.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.author.pal.statement.CreateUserOrObjectStatement;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.events.CreateObjectAttributeEvent;
import gov.nist.csd.pm.policy.events.EventContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.PMRuntimeException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.pap.naming.Naming;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_PC;
import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static gov.nist.csd.pm.policy.model.obligation.event.Performs.events;
import static org.junit.jupiter.api.Assertions.*;

class EPPTest {

    @Test
    void test() throws PMException {
        PAP pap = new MemoryPAP();
        PDP pdp = new PDP(pap, new MemoryPolicyReviewer());
        EPP epp = new EPP(pdp, pap);

        pap.graph().createPolicyClass("pc1", noprops());
        pap.graph().createObjectAttribute("oa1", noprops(), Naming.baseObjectAttribute("pc1"));

        String pal = """
                create obligation 'test' {
                    create rule 'rule1'
                    when any user
                    performs 'create_object_attribute'
                    on 'oa1'
                    do(evtCtx) {
                        create policy class 'pc2';
                    }
                }
                """;
        new PALExecutor(pap).compileAndExecutePAL(new UserContext(SUPER_USER), pal);

        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("oa1"));

        pdp.runTx(new UserContext(SUPER_USER), (txPDP) -> txPDP.graph().createObjectAttribute("oa2", noprops(), "oa1"));

        assertTrue(pap.graph().nodeExists("pc2"));

    }

    @Test
    void testAccessingEventContextInResponse() throws PMException {
        PAP pap = new MemoryPAP();
        PDP pdp = new PDP(pap, new MemoryPolicyReviewer());
        EPP epp = new EPP(pdp, pap);

        pap.graph().createPolicyClass("pc1", noprops());
        pap.graph().createObjectAttribute("oa1", noprops(), Naming.baseObjectAttribute("pc1"));

        String pal = """
                create obligation 'test' {
                    create rule 'rule1'
                    when any user
                    performs 'create_object_attribute'
                    on 'oa1'
                    do(evtCtx) {
                        create policy class evtCtx['eventName'];
                        let target = evtCtx['target'];
                        
                        let event = evtCtx['event'];
                        create policy class concat([event['name'], '_test']);
                        set properties of event['name'] to {'key': target};
                        
                        let userCtx = evtCtx['userCtx'];
                        create policy class concat([userCtx['user'], '_test']);
                    }
                }
                """;
        new PALExecutor(pap).compileAndExecutePAL(new UserContext(SUPER_USER), pal);

        pdp.runTx(new UserContext(SUPER_USER), (txPDP) -> txPDP.graph().createObjectAttribute("oa2", noprops(), "oa1"));

        assertTrue(pap.graph().getPolicyClasses().containsAll(Arrays.asList(
                "super_pc", "pc1", "create_object_attribute", "oa2_test", "super_test"
        )));
    }

    @Test
    void testErrorInEPPResponse() throws PMException {
        PAP pap = new MemoryPAP();
        PDP pdp = new PDP(pap, new MemoryPolicyReviewer());
        EPP epp = new EPP(pdp, pap);

        pdp.runTx(new UserContext(SUPER_USER), (policy) -> {
            policy.graph().createPolicyClass("pc1", noprops());
            policy.graph().createUserAttribute("ua1", noprops(), Naming.baseUserAttribute("pc1"));
            policy.graph().createObjectAttribute("oa1", noprops(), Naming.baseObjectAttribute("pc1"));
            policy.graph().createUser("u1", noprops(), "ua1");
            policy.graph().createObject("o1", noprops(), "oa1");
            policy.graph().associate("ua1", Naming.baseObjectAttribute(SUPER_PC), new AccessRightSet(CREATE_OBLIGATION));
            policy.graph().associate("ua1", "oa1", new AccessRightSet(CREATE_OBJECT));
        });

        pdp.runTx(new UserContext("u1"), (policy) -> {
            policy.obligations().create(new UserContext("u1"), "test",
                    new Rule("rule1",
                            new EventPattern(EventSubject.anyUser(), events(CREATE_OBJECT_ATTRIBUTE)),
                            new Response(new UserContext("u1"),
                                    new CreateUserOrObjectStatement(new Expression(new Literal("o2")), NodeType.O, new Expression(new Literal("oa1"))),
                                    new CreatePolicyStatement(new Expression(new Literal("pc2"))))
                    )
            );
        });

        assertThrows(PMRuntimeException.class, () -> epp.handlePolicyEvent(new EventContext(new UserContext(SUPER_USER), new CreateObjectAttributeEvent("oa2", new HashMap<>(), "pc1"))));

        assertFalse(pap.graph().nodeExists("o2"));
        assertFalse(pap.graph().nodeExists("pc2"));
    }
}