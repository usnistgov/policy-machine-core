package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.exception.*;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.query.model.explain.*;
import gov.nist.csd.pm.common.routine.Routine;
import gov.nist.csd.pm.pdp.adjudication.AdjudicationResponse;
import gov.nist.csd.pm.pdp.adjudication.Decision;
import gov.nist.csd.pm.pdp.adjudication.OperationRequest;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.PAPTest.testAdminPolicy;
import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_OBJECT_ATTRIBUTE;
import static gov.nist.csd.pm.common.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.common.op.graph.GraphOp.ASCENDANT_OPERAND;
import static gov.nist.csd.pm.common.op.graph.GraphOp.DESCENDANTS_OPERAND;
import static gov.nist.csd.pm.pdp.adjudication.Decision.DENY;
import static gov.nist.csd.pm.pdp.adjudication.Decision.GRANT;


import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static gov.nist.csd.pm.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.*;

class PDPTest {

    @Test
    void testRunTx() throws PMException {
        PAP pap = new TestPAP();
        PDP pdp = new PDP(pap);

        pap.runTx(txPAP -> {
            long pc1 = txPAP.modify().graph().createPolicyClass("pc1");
            long ua1 = txPAP.modify().graph().createUserAttribute("ua1", List.of(pc1));
            long oa1 = txPAP.modify().graph().createObjectAttribute("oa1", List.of(pc1));
            long oa2 = txPAP.modify().graph().createObjectAttribute("oa2", List.of(pc1));
            txPAP.modify().graph().createUser("u1", List.of(ua1));
            txPAP.modify().graph().createObject("o1", List.of(oa1));
        });

        PMException e = assertThrows(
                PMException.class,
                () -> pdp.runTx(
                        new TestUserContext("u1"),
                        policy -> {
                            policy.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet(CREATE_OBJECT_ATTRIBUTE));
                            return null;
                        }
                )
        );
        assertEquals("{user: " + id("u1") + "} does not have access right [associate] on " + new TargetContext(id("ua1")), e.getMessage());

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("oa1"));
    }


    @Test
    void testBootstrapWithAdminPolicyOnly() throws PMException {
        PAP pap = new TestPAP();
        PDP pdp = new PDP(pap);

        pdp.bootstrap(p -> {
            p.modify().graph().createPolicyClass("pc1");
        });

        testAdminPolicy(pap);
        assertTrue(pap.query().graph().nodeExists("pc1"));
    }

    @Test
    void testBootstrapWithExistingPolicyThrowsException() throws PMException {
        PAP pap = new TestPAP();
        PDP pdp = new PDP(pap);
        pap.modify().graph().createPolicyClass("pc1");
        assertThrows(BootstrapExistingPolicyException.class, () -> {
            pdp.bootstrap((policy) -> {});
        });

        pap.reset();

        pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        pap.modify().graph().createObject("o1", List.of(oa1));

        pap.modify().prohibitions().createProhibition("pro1", new ProhibitionSubject(u1),
                new AccessRightSet("read"),
                true,
                Collections.singleton(new ContainerCondition(id("oa1"), false)));

        assertThrows(BootstrapExistingPolicyException.class, () -> {
            pdp.bootstrap((policy) -> {});
        });

        pap.modify().obligations().createObligation(id("u1"), "obl1", List.of());

        assertThrows(BootstrapExistingPolicyException.class, () -> {
            pdp.bootstrap((policy) -> {});
        });
    }

    @Test
    void testRollback() throws PMException {
        PAP pap = new TestPAP();
        PDP pdp = new PDP(pap);
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long oa1 = pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));
        pap.modify().graph().associate(ua1, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), new AccessRightSet("*"));

        assertThrows(NodeNameExistsException.class, () -> {
            pdp.runTx(new TestUserContext("u1"), policy -> {
                long pc2 = policy.modify().graph().createPolicyClass("pc2");
                // expect error and rollback
                policy.modify().graph().createObjectAttribute("oa1", List.of(pc2));
                return null;
            });
        });

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("ua1"));
        assertTrue(pap.query().graph().nodeExists("oa1"));
        assertFalse(pap.query().graph().nodeExists("pc2"));
    }

    @Test
    void testAdjudicateResourceOperation() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                set resource operations ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["read"]
                
                create u "u1" in ["ua1"]
                create o "o1" in ["oa1"]
                """);

        PDP pdp = new PDP(pap);
        pdp.setExplain(true);

        AdjudicationResponse resp = pdp.adjudicateResourceOperation(new TestUserContext("u1"), id("o1"), "read");
        assertEquals(resp.getValue(), pap.query().graph().getNodeByName("o1"));
        assertEquals(resp.getDecision(), GRANT);
        assertNull(resp.getExplain());

        resp = pdp.adjudicateResourceOperation(new TestUserContext("u1"), id("o1"), "write");
        assertNull(resp.getValue());
        assertEquals(resp.getDecision(), Decision.DENY);
        assertEquals(new Explain(
                new AccessRightSet("read"),
                List.of(
                        new PolicyClassExplain(
                                pap.query().graph().getNodeByName("pc1"),
                                new AccessRightSet("read"),
                                List.of(
                                        List.of(
                                                new ExplainNode(pap.query().graph().getNodeByName("o1"), List.of()),
                                                new ExplainNode(
                                                        pap.query().graph().getNodeByName("oa1"),
                                                        List.of(
                                                                new ExplainAssociation(pap.query().graph().getNodeByName("ua1"), new AccessRightSet("read"),
                                                                        List.of(new Path(pap.query().graph().getNodeByName("u1"), pap.query().graph().getNodeByName("ua1"))))
                                                        )),
                                                new ExplainNode(pap.query().graph().getNodeByName("pc1"), List.of())
                                        )
                                )
                        )
                )
        ), resp.getExplain());
    }

    @Test
    void testAdjudicateAdminOperation() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(new UserContext(0), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                associate "ua1" and "oa1" with ["assign"]
                associate "ua1" and "oa2" with ["assign_to"]
                
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                create o "o1" in ["oa1"]
                
                operation op1() string {
                    check "assign_to" on "oa2"
                } {
                    create pc "test"
                    return "test"
                }
                """);

        PDP pdp = new PDP(pap);
        pdp.setExplain(true);

        // builtin operation
        assertThrows(PMException.class, () -> {
            pdp.adjudicateAdminOperation(
                    new TestUserContext("u1"),
                    "assign", Map.of(ASCENDANT_OPERAND, id("o1"), DESCENDANTS_OPERAND, ids("oa2"))
            );
        });

        // custom operation
        AdjudicationResponse resp = pdp.adjudicateAdminOperation(new TestUserContext("u1"), "op1", Map.of());
        assertEquals(GRANT, resp.getDecision());
        assertEquals("test", resp.getValue());

        // denied
        resp = pdp.adjudicateAdminOperation(new UserContext(id("u2")), "op1", Map.of());
        assertEquals(Decision.DENY, resp.getDecision());
        assertEquals(new Explain(
                new AccessRightSet(),
                List.of(
                        new PolicyClassExplain(
                                pap.query().graph().getNodeByName("pc1"),
                                new AccessRightSet(),
                                List.of(
                                        List.of(
                                                new ExplainNode(pap.query().graph().getNodeByName("oa2"), List.of(
                                                        new ExplainAssociation(pap.query().graph().getNodeByName("ua1"), new AccessRightSet("assign_to"), List.of())
                                                )),
                                                new ExplainNode(pap.query().graph().getNodeByName("pc1"), List.of())
                                        )
                                )
                        )
                )
        ), resp.getExplain());
    }

    @Test
    void testAdjudicateDoesNotExist() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(new UserContext(0), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                
                set resource operations ["read", "write"]

                """);
        PDP pdp = new PDP(pap);
        assertThrows(OperationDoesNotExistException.class,
                () -> pdp.adjudicateAdminOperation(new TestUserContext("u1"), "op1", Map.of()));
        assertThrows(NodeDoesNotExistException.class,
                () -> pdp.adjudicateResourceOperation(new TestUserContext("u1"), id("oa1"), "read"));
        assertThrows(OperationDoesNotExistException.class,
                () -> pdp.adjudicateResourceOperation(new TestUserContext("u1"), id("ua1"), "x"));
    }

    @Test
    void testAdjudicateAdminRoutine() throws PMException {
        PAP pap = new TestPAP();
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        pap.modify().graph().createUser("u1", List.of(ua1));
        pap.modify().graph().createUser("u2", List.of(ua2));
        pap.modify().graph().associate(ua1, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), new AccessRightSet("*"));

        pap.modify().routines().createAdminRoutine(new Routine<String>("routine1", List.of("a")) {
            @Override
            public String execute(PAP pap, Map<String, Object> operands) throws PMException {
                pap.modify().graph().createPolicyClass((String) operands.get("a"));
                return "test1";
            }
        });
        pap.executePML(new TestUserContext("u1"), """
                routine routine2() map[string]string {
                    create policy class "test2"
                    return {"test2": "test2"}
                }
                """);

        PDP pdp = new PDP(pap);
        pdp.setExplain(true);

        AdjudicationResponse response = pdp.adjudicateAdminRoutine(new TestUserContext("u1"), "routine1", Map.of("a", "test"));
        assertEquals(GRANT, response.getDecision());
        assertEquals("test1", response.getValue());
        response = pdp.adjudicateAdminRoutine(new TestUserContext("u1"), "routine2", Map.of());
        assertEquals(GRANT, response.getDecision());
        assertEquals(Map.of("test2", "test2"), response.getValue());

        assertTrue(pap.query().graph().nodeExists("test"));
        assertTrue(pap.query().graph().nodeExists("test2"));

        response = pdp.adjudicateAdminRoutine(new UserContext(id("u2")), "routine1", Map.of("a", "test3"));
        assertEquals(DENY, response.getDecision());
        assertEquals(new Explain(
                new AccessRightSet(),
                List.of(
                        new PolicyClassExplain(
                                pap.query().graph().getNodeByName("PM_ADMIN"),
                                new AccessRightSet(),
                                List.of(
                                        List.of(
                                                new ExplainNode(pap.query().graph().getNodeById(AdminPolicyNode.PM_ADMIN_OBJECT.nodeId()), List.of(
                                                        new ExplainAssociation(pap.query().graph().getNodeByName("ua1"), new AccessRightSet("*"), List.of())
                                                )),
                                                new ExplainNode(pap.query().graph().getNodeByName("PM_ADMIN"), List.of())
                                        )
                                )
                        )
                )
        ), response.getExplain());
    }

    @Test
    void testRoutineWithForLoop() throws PMException {
        String pml = """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                create u "u1" in ["ua1"]
                
                associate "ua1" and "oa1" with ["create_object"]
                
                routine routine1() {
                    foreach x in ["oa1", "oa2"] {
                        if true {
                            create o x + "_o" in [x]
                        }
                    }
                }
                """;
        PAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        AdjudicationResponse response = pdp.adjudicateAdminRoutine(new TestUserContext("u1"), "routine1", Map.of());
        assertEquals(DENY, response.getDecision());
    }

    @Test
    void testRoutineTx() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                associate "ua1" and "oa1" with ["create_object"]
                
                create u "u1" in ["ua1"]
                
                routine r1() {
                    create o "o1" in ["oa1"]
                    create o "o2" in ["oa1"]
                    create o "o3" in ["oa2"]
                }
                """);
        PDP pdp = new PDP(pap);
        assertThrows(UnauthorizedException.class, () -> pdp.runTx(new TestUserContext("u1"), tx -> {
            tx.executePML(new TestUserContext("u1"), "r1()");
            return null;
        }));
        assertFalse(pap.query().graph().nodeExists("o1"));
        assertFalse(pap.query().graph().nodeExists("o2"));

    }

    @Test
    void testPMLOperationDoesNotPublishEvents() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                associate "ua1" and "oa1" with ["create_object"]
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]
                
                create u "u1" in ["ua1"]
                
                operation op1() {
                    create pc "pc2"
                    foreach x in ["ua2", "ua3"] {
                        create ua x in ["pc2"]
                    }
                }
                
                create obligation "o1" {
                    create rule "r1"
                    when any user
                    performs "create_user_attribute"
                    do(ctx) {
                        create pc "test"
                    }
                }
                """);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        pdp.adjudicateAdminOperation(new TestUserContext("u1"), "op1", Map.of());
        assertFalse(pap.query().graph().nodeExists("test"));
    }

    @Test
    void testAdjudicateRoutineListOfOperations() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                
                associate "ua1" and PM_ADMIN_OBJECT with ["create_policy_class", "create_object_attribute"]
                
                create u "u1" in ["ua1"]
                
                operation op1(string name) {
                    create pc name
                }
                
                create obligation "obl1" {
                    create rule "rule1"
                    when any user
                    performs "op1"
                    do(ctx) {
                        create oa "oa_" + ctx.operands.name in [ctx.operands.name]
                    }
                }
                """);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);

        AdjudicationResponse response = pdp.adjudicateAdminRoutine(new TestUserContext("u1"), List.of(
                new OperationRequest("op1", Map.of("name", "pc2")),
                new OperationRequest("op1", Map.of("name", "pc3"))
        ));

        assertNull(response.getValue());

        assertTrue(pap.query().graph().nodeExists("pc2"));
        assertTrue(pap.query().graph().nodeExists("pc3"));
        assertTrue(pap.query().graph().nodeExists("oa_pc3"));
        assertTrue(pap.query().graph().nodeExists("oa_pc3"));
    }

    @Test
    void testExplainFalseDoesNotIncludeExplainInResponse() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                
                create o "test" in ["oa1"]
                
                create u "u1" in ["ua1"]
               
                operation op1(nodeop string name) {
                    check "create_policy_class" on name
                } {}
               
                """);

        PDP pdp = new PDP(pap);
        AdjudicationResponse response = pdp.adjudicateAdminOperation(new TestUserContext("u1"), "op1", Map.of(NAME_OPERAND, "test"));
        assertEquals(response.getDecision(), DENY);
        assertNull(response.getExplain());
    }
}