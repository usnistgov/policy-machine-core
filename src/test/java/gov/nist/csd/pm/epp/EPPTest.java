package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.pap.graph.node.NodeType;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.obligation.EventContext;
import gov.nist.csd.pm.pap.obligation.EventPattern;
import gov.nist.csd.pm.pap.obligation.Response;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateNonPCStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreatePolicyStatement;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.*;
import gov.nist.csd.pm.pdp.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.*;
import static gov.nist.csd.pm.pap.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.pap.op.graph.GraphOp.DESCENDANTS_OPERAND;
import static gov.nist.csd.pm.pdp.Decision.GRANT;
import static org.junit.jupiter.api.Assertions.*;

class EPPTest {

    @Test
    void testCustomOperationEvent() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                
                operation op1(nodeop string a, nodeop []string b) {
                    
                }
                
                create obligation "obl1" {
                    create rule "op1"
                    when any user
                    performs "op1"
                    on {
                        a: "oa1",
                        b: "oa1"
                    }
                    do(ctx) {
                        create pc ctx.operands.a + "pc1"
                        
                        foreach x in ctx.operands.b {
                            create pc x + "pc2"
                        }
                    }
                    
                    create rule "op2"
                    when any user
                    performs "op2"
                    on {
                        a: "oa2",
                        b: "oa2"
                    }
                    do(ctx) {
                        create pc ctx.operands.a + "pc1"
                        
                        foreach x in ctx.operands.b {
                            create pc x + "pc2"
                        }
                    }
                }
                """);

        pap.modify().operations().createAdminOperation(new Operation<>("op2", List.of("a", "b"), List.of("a", "b")) {
            @Override
            public Object execute(PAP pap, Map operands) throws PMException {
                return null;
            }

            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map operands) throws PMException {

            }
        });

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);

        AdminAdjudicationResponse response = pdp.adjudicateAdminOperation(
                new UserContext("u1"),
                "op1", Map.of("a", "oa1", "b", List.of("oa1", "oa2"))
        );
        assertEquals(Decision.DENY, response.getDecision());

        pap.modify().graph().associate("ua1", AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(), new AccessRightSet("*a"));

        response = pdp.adjudicateAdminOperation(
                new UserContext("u1"),
                "op1", Map.of("a", "oa1", "b", List.of("oa1", "oa2"))
        );
        assertEquals(GRANT, response.getDecision());

        response = pdp.adjudicateAdminOperation(
                new UserContext("u1"),
                "op2", Map.of("a", "oa2", "b", "oa2")
        );
        assertEquals(GRANT, response.getDecision());

        assertTrue(pap.query().graph().nodeExists("oa1pc1"));
        assertTrue(pap.query().graph().nodeExists("oa1pc2"));
        assertTrue(pap.query().graph().nodeExists("oa2pc2"));
        assertTrue(pap.query().graph().nodeExists("oa2pc1"));
        assertTrue(pap.query().graph().nodeExists("oa2pc2"));
    }

    @Test
    void testResourceOperationEvent() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                
                set resource operations ["read"]
                
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                
                associate "ua1" and "oa1" with ["read"]
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]
                
                create obligation "obl1" {
                    create rule "op1"
                    when any user
                    performs "read"
                    on {
                        target: "oa1"
                    }
                    do(ctx) {
                        create pc ctx.operands.target + "pc1"
                    }
                }
                """);
        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        ResourceAdjudicationResponse response = pdp.adjudicateResourceOperation(new UserContext("u1"), "oa1", "read");
        assertEquals(GRANT, response.getDecision());

        assertTrue(pap.query().graph().nodeExists("oa1pc1"));
    }

    @Test
    void test() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);

        String pml = """
                create pc "pc1"
                create oa "oa1" in ["pc1"]
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                associate "ua1" and "oa1" with ["*"]
                associate "ua1" and PM_ADMIN_OBJECT with ["*"]
                create obligation "test" {
                    create rule "rule1"
                    when any user
                    performs "create_object_attribute"
                    on {
                        descendants: "oa1"
                    }
                    do(evtCtx) {
                        create policy class "pc2"
                    }
                }
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("oa1"));

        pdp.runTx(new UserContext("u1"), (txPDP) -> txPDP.modify().graph().createObjectAttribute("oa2", List.of("oa1")));

        assertTrue(pap.query().graph().nodeExists("pc2"));

    }

    @Test
    void testAccessingEventContextInResponse() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);

        String pml = """                
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create oa "oa1" in ["pc1"]
                
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]
                
                create obligation "test" {
                    create rule "rule1"
                    when any user
                    performs "create_object_attribute"
                    on {
                        descendants: "oa1"
                    }
                    do(ctx) {
                        name := ctx.opName
                        create policy class name

                        name = ctx.operands.name
                        create policy class name + "_test"
                        set properties of name + "_test" to {"key": name}

                        userCtx := ctx["user"]
                        create policy class ctx["user"] + "_test"
                    }
                }
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        pdp.runTx(new UserContext("u1"), (txPDP) -> txPDP.modify().graph().createObjectAttribute("oa2",
                List.of("oa1")));

        assertTrue(pap.query().graph().getPolicyClasses().containsAll(Arrays.asList(
                "pc1", "create_object_attribute", "oa2_test", "u1_test"
        )));
    }

    @Test
    void testErrorInEPPResponse() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);

        pap.runTx((txPAP) -> {
            txPAP.modify().graph().createPolicyClass("pc1");
            txPAP.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            txPAP.modify().graph().createUserAttribute("ua2", List.of("pc1"));
            txPAP.modify().graph().associate("ua2", "ua1", new AccessRightSet("*"));
            txPAP.modify().graph().associate("ua2", AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(), new AccessRightSet("*"));
            txPAP.modify().graph().createObjectAttribute("oa1",  List.of("pc1"));
            txPAP.modify().graph().createUser("u1",  List.of("ua1", "ua2"));
            txPAP.modify().graph().createObject("o1",  List.of("oa1"));
            txPAP.modify().graph().associate("ua1", AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(),
                    new AccessRightSet(CREATE_OBLIGATION));
            txPAP.modify().graph().associate("ua1", "oa1", new AccessRightSet(CREATE_OBJECT));
            txPAP.modify().graph().associate("ua1", AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(), new AccessRightSet("*"));
        });

        pdp.runTx(new UserContext("u1"), (policy) -> {
            policy.modify().obligations().createObligation("u1", "test",
                    List.of(new Rule(
                            "rule1",
                            new EventPattern(new SubjectPattern(), new OperationPattern(CREATE_OBJECT_ATTRIBUTE)),
                            new Response("evtCtx", List.of(
                                    new CreateNonPCStatement(
                                            new StringLiteral("o2"),
                                            NodeType.O,
                                            new ArrayLiteral(new Expression[]{new StringLiteral("oa1")}, Type.string())
                                    ),

                                    // expect error for node already exists
                                    new CreatePolicyStatement(new StringLiteral("pc1"))
                            ))
                    ))
            );

            return null;
        });

        EventContext eventCtx = new EventContext(
                "u1",
                CREATE_OBJECT_ATTRIBUTE,
                Map.of(
                        NAME_OPERAND, "oa2",
                        DESCENDANTS_OPERAND, List.of("pc1")
                )
        );

        assertThrows(PMException.class, () -> {
            epp.getEventProcessor().processEvent(eventCtx);
        });

        assertFalse(pap.query().graph().nodeExists("o2"));
        assertFalse(pap.query().graph().nodeExists("pc2"));
    }

    @Test
    void testCustomFunctionInResponse() throws PMException {
        MemoryPAP pap = new MemoryPAP();

        PMLOperation pmlOperation = new PMLOperation("testFunc", Type.voidType()) {
            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

            }

            @Override
            public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
                pap.modify().graph().createPolicyClass("test");

                return new VoidValue();
            }
        };

        pap.setPMLOperations(pmlOperation);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);

        String pml = """                
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create oa "oa1" in ["pc1"]
                
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and PM_ADMIN_OBJECT with ["create_policy_class"]
                
                create obligation "test" {
                    create rule "rule1"
                    when any user
                    performs "create_object_attribute"
                    on {
                        descendants: "oa1"
                    }
                    do(evtCtx) {
                        testFunc()
                    }
                }
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        pdp.runTx(new UserContext("u1"), (txPDP) -> {
            txPDP.modify().graph().createObjectAttribute("oa2", List.of("oa1"));

            return null;
        });

        assertTrue(pap.query().graph().nodeExists("test"));
    }

    @Test
    void testReturnInResponse() throws PMException {
        MemoryPAP pap = new MemoryPAP();

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);

        String pml = """                
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create oa "oa1" in ["pc1"]
                
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and PM_ADMIN_OBJECT with ["create_policy_class"]
                
                create obligation "test" {
                    create rule "rule1"
                    when any user
                    performs "create_object_attribute"
                    on {
                        descendants: "oa1"
                    }
                    do(evtCtx) {
                        if true {
                            return
                        }
                        
                        create policy class "test"
                    }
                }
                """;
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        pdp.runTx(new UserContext("u1"), (txPDP) -> txPDP.modify().graph().createObjectAttribute("oa2", List.of("oa1")));
        assertFalse(pap.query().graph().nodeExists("test"));
    }

    @Test
    void testErrorInResponseOperation() throws PMException {
        String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]
                associate "ua2" and PM_ADMIN_OBJECT with ["*a"]
                
                operation op1() {
                    check "assign" on "oa1"
                } {
                    create pc "test_pc"
                }
                
                routine routine1() {
                    create o "o1" in ["oa1"]
                }
                
                create obligation "obl1" {
                    create rule "rule1"
                    when any user
                    performs "create_policy_class"
                    do(ctx) {
                        op1()
                        routine1()
                    }
                }
                """;
        // as u1 - ok
        MemoryPAP pap = new MemoryPAP();
        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        pap.executePML(new UserContext("u1"), pml);
        pdp.runTx(new UserContext("u1"), (txPDP) -> txPDP.modify().graph().createPolicyClass("u1_pc"));
        assertTrue(pap.query().graph().nodeExists("test_pc"));
        assertTrue(pap.query().graph().nodeExists("o1"));
        assertTrue(pap.query().graph().nodeExists("u1_pc"));

        // as u2 fail
        pap = new MemoryPAP();
        PDP pdp2 = new PDP(pap);
        epp = new EPP(pdp2, pap);
        pap.executePML(new UserContext("u2"), pml);
        assertThrows(UnauthorizedException.class, () -> pdp2.runTx(new UserContext("u2"), (txPDP) -> txPDP.modify().graph().createPolicyClass("u2_pc")));
        assertFalse(pap.query().graph().nodeExists("test_pc"));
        assertFalse(pap.query().graph().nodeExists("o1"));
        assertFalse(pap.query().graph().nodeExists("u1_pc"));
    }
}