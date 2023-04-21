package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pdp.memory.MemoryPDP;
import gov.nist.csd.pm.policy.pml.model.expression.ArrayLiteral;
import gov.nist.csd.pm.policy.pml.model.expression.Literal;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.VariableReference;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.pml.statement.CreateUserOrObjectStatement;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.events.CreateObjectAttributeEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.PMRuntimeException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.serializer.PALDeserializer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static gov.nist.csd.pm.pap.SuperPolicy.*;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.obligation.event.Performs.events;
import static org.junit.jupiter.api.Assertions.*;

class EPPTest {

    @Test
    void test() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        PDP pdp = new MemoryPDP(pap, false);
        EPP epp = new EPP(pdp, pap);

        String pal = """
                create pc 'pc1';
                create oa 'oa1' in ['pc1'];
                create obligation 'test' {
                    create rule 'rule1'
                    when any user
                    performs ['create_object_attribute']
                    on 'oa1'
                    do(evtCtx) {
                        create policy class 'pc2';
                    }
                }
                """;
        pap.fromString(pal, new PALDeserializer(new UserContext(SUPER_USER)));

        assertTrue(pap.nodeExists("pc1"));
        assertTrue(pap.nodeExists("oa1"));

        pdp.runTx(new UserContext(SUPER_USER), (txPDP) -> txPDP.createObjectAttribute("oa2", "oa1"));

        assertTrue(pap.nodeExists("pc2"));

    }

    @Test
    void testAccessingEventContextInResponse() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        PDP pdp = new MemoryPDP(pap, false);
        EPP epp = new EPP(pdp, pap);

        String pal = """
                create pc 'pc1';
                create oa 'oa1' in ['pc1'];
                create obligation 'test' {
                    create rule 'rule1'
                    when any user
                    performs ['create_object_attribute']
                    on 'oa1'
                    do(evtCtx) {
                        create policy class evtCtx['eventName'];
                        let target = evtCtx['target'];
                        
                        create policy class concat([evtCtx['event']['name'], '_test']);
                        set properties of evtCtx['event']['name'] to {'key': target};
                        
                        let userCtx = evtCtx['userCtx'];
                        create policy class concat([userCtx['user'], '_test']);
                    }
                }
                """;
        pap.fromString(pal, new PALDeserializer(new UserContext(SUPER_USER)));

        pdp.runTx(new UserContext(SUPER_USER), (txPDP) -> txPDP.createObjectAttribute("oa2", "oa1"));
        assertTrue(pap.getPolicyClasses().containsAll(Arrays.asList(
                SUPER_PC, "pc1", "create_object_attribute", "oa2_test", "super_test"
        )));
    }

    @Test
    void testErrorInEPPResponse() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        PDP pdp = new MemoryPDP(pap, false);
        EPP epp = new EPP(pdp, pap);

        pdp.runTx(new UserContext(SUPER_USER), (policy) -> {
            policy.createPolicyClass("pc1");
            policy.createUserAttribute("ua1", "pc1");
            policy.createObjectAttribute("oa1", "pc1");
            policy.createUser("u1", "ua1");
            policy.createObject("o1", "oa1");
            policy.associate("ua1", SUPER_OA, new AccessRightSet(CREATE_OBLIGATION));
            policy.associate("ua1", "oa1", new AccessRightSet(CREATE_OBJECT));
        });

        pdp.runTx(new UserContext("u1"), (policy) -> {
            policy.createObligation(new UserContext("u1"), "test",
                    new Rule("rule1",
                            new EventPattern(EventSubject.anyUser(), events(CREATE_OBJECT_ATTRIBUTE)),
                            new Response(new UserContext("u1"),
                                    new CreateUserOrObjectStatement(
                                            new Expression(new VariableReference("o2", Type.string())),
                                            NodeType.O,
                                            new Expression(new Literal(new ArrayLiteral(new Expression[]{new Expression(new VariableReference("oa1", Type.string()))}, Type.string())))
                                    ),
                                    new CreatePolicyStatement(new Expression(new VariableReference("pc2", Type.string()))))
                    )
            );
        });

        EventContext eventCtx = new EventContext(new UserContext(SUPER_USER), new CreateObjectAttributeEvent("oa2", new HashMap<>(), "pc1"));
        assertThrows(PMRuntimeException.class, () -> {
            epp.handlePolicyEvent(eventCtx);
        });

        assertFalse(pap.nodeExists("o2"));
        assertFalse(pap.nodeExists("pc2"));
    }
}