package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.pap.AdminPolicyNode;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.value.VoidValue;
import gov.nist.csd.pm.policy.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.policy.model.obligation.event.subject.AnyUserSubject;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.statement.CreateNonPCStatement;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.events.graph.CreateObjectAttributeEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.obligation.event.Performs.events;
import static org.junit.jupiter.api.Assertions.*;

class EPPTest {

    @Test
    void test() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);

        String pml = """
                create pc "pc1"
                create oa "oa1" assign to ["pc1"]
                create ua "ua1" assign to ["pc1"]
                create u "u1" assign to ["ua1"]
                associate "ua1" and "oa1" with ["*"]
                associate "ua1" and POLICY_CLASS_TARGETS with ["*"]
                create obligation "test" {
                    create rule "rule1"
                    when any user
                    performs ["create_object_attribute"]
                    on ["oa1"]
                    do(evtCtx) {
                        create policy class "pc2"
                    }
                }
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("oa1"));

        pdp.runTx(new UserContext("u1"), (txPDP) -> txPDP.graph().createObjectAttribute("oa2", "oa1"));

        assertTrue(pap.graph().nodeExists("pc2"));

    }

    @Test
    void testAccessingEventContextInResponse() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);

        String pml = """                
                create pc "pc1"
                create ua "ua1" assign to ["pc1"]
                create u "u1" assign to ["ua1"]
                create oa "oa1" assign to ["pc1"]
                
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and POLICY_CLASS_TARGETS with [create_policy_class]
                
                create obligation "test" {
                    create rule "rule1"
                    when any user
                    performs ["create_object_attribute"]
                    on ["oa1"]
                    do(evtCtx) {
                        create policy class evtCtx["eventName"]
                        target := evtCtx["target"]
                        
                        create policy class evtCtx["event"]["name"] + "_test"
                        set properties of evtCtx["event"]["name"] to {"key": target}
                        
                        userCtx := evtCtx["userCtx"]
                        create policy class userCtx["user"] + "_test"
                    }
                }
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        pdp.runTx(new UserContext("u1"), (txPDP) -> txPDP.graph().createObjectAttribute("oa2", "oa1"));
        assertTrue(pap.graph().getPolicyClasses().containsAll(Arrays.asList(
                "pc1", "create_object_attribute", "oa2_test", "u1_test"
        )));
    }

    @Test
    void testErrorInEPPResponse() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);

        pap.runTx((policy) -> {
            policy.graph().createPolicyClass("pc1");
            policy.graph().createUserAttribute("ua1", "pc1");
            policy.graph().createUserAttribute("ua2", "pc1");
            policy.graph().associate("ua2", "ua1", new AccessRightSet("*"));
            policy.graph().associate("ua2", AdminPolicyNode.OBLIGATIONS_TARGET.nodeName(), new AccessRightSet("*"));
            policy.graph().createObjectAttribute("oa1", "pc1");
            policy.graph().createUser("u1", "ua1", "ua2");
            policy.graph().createObject("o1", "oa1");
            policy.graph().associate("ua1", AdminPolicyNode.ADMIN_POLICY_TARGET.nodeName(),
                                     new AccessRightSet(CREATE_OBLIGATION));
            policy.graph().associate("ua1", "oa1", new AccessRightSet(CREATE_OBJECT));
            policy.graph().associate("ua1", AdminPolicyNode.OBLIGATIONS_TARGET.nodeName(), new AccessRightSet("*"));
        });

        pdp.runTx(new UserContext("u1"), (policy) -> {
            policy.obligations().create(new UserContext("u1"), "test",
                    new Rule("rule1",
                             new EventPattern(new AnyUserSubject(), events(CREATE_OBJECT_ATTRIBUTE)),
                             new Response("evtCtx", List.of(
                                     new CreateNonPCStatement(
                                             new StringLiteral("o2"),
                                             NodeType.O,
                                             new ArrayLiteral(new Expression[]{new StringLiteral("oa1")}, Type.string())
                                     ),
                                     new CreatePolicyStatement(new StringLiteral("pc2"))
                             ))
                    )
            );
        });

        EventContext eventCtx = new EventContext(new UserContext("u1"), new CreateObjectAttributeEvent("oa2", new HashMap<>(), "pc1"));
        assertThrows(PMException.class, () -> {
            epp.getEventProcessor().processEvent(eventCtx);
        });

        assertFalse(pap.graph().nodeExists("o2"));
        assertFalse(pap.graph().nodeExists("pc2"));
    }

    @Test
    void testCustomFunctionInResponse() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());

        FunctionDefinitionStatement testFunc = new FunctionDefinitionStatement.Builder("testFunc")
                .returns(Type.voidType())
                .executor((ctx, policy) -> {
                    policy.graph().createPolicyClass("test");

                    return new VoidValue();
                })
                .build();

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap, testFunc);

        String pml = """                
                create pc "pc1"
                create ua "ua1" assign to ["pc1"]
                create u "u1" assign to ["ua1"]
                create oa "oa1" assign to ["pc1"]
                
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and POLICY_CLASS_TARGETS with [create_policy_class]
                
                create obligation "test" {
                    create rule "rule1"
                    when any user
                    performs ["create_object_attribute"]
                    on ["oa1"]
                    do(evtCtx) {
                        testFunc()
                    }
                }
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer(testFunc));

        pdp.runTx(new UserContext("u1"), (txPDP) -> txPDP.graph().createObjectAttribute("oa2", "oa1"));
        assertTrue(pap.graph().nodeExists("test"));
    }

    @Test
    void testReturnInResponse() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);

        String pml = """                
                create pc "pc1"
                create ua "ua1" assign to ["pc1"]
                create u "u1" assign to ["ua1"]
                create oa "oa1" assign to ["pc1"]
                
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and POLICY_CLASS_TARGETS with [create_policy_class]
                
                create obligation "test" {
                    create rule "rule1"
                    when any user
                    performs ["create_object_attribute"]
                    on ["oa1"]
                    do(evtCtx) {
                        if true {
                            return
                        }
                        
                        create policy class "test"
                    }
                }
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        pdp.runTx(new UserContext("u1"), (txPDP) -> txPDP.graph().createObjectAttribute("oa2", "oa1"));
        assertFalse(pap.graph().nodeExists("test"));
    }
}