package gov.nist.csd.pm.core.pdp;

import static gov.nist.csd.pm.core.pap.PAPTest.testAdminPolicy;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_OBJECT_ATTRIBUTE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.function.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.BootstrapExistingPolicyException;
import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.NodeNameExistsException;
import gov.nist.csd.pm.core.common.exception.OperationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.Routine;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.AnyOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.adjudication.OperationRequest;
import gov.nist.csd.pm.core.pdp.bootstrap.PolicyBootstrapper;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

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
        assertEquals("{user: u1} missing required access rights {associate} on {target: ua1}", e.getMessage());

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("oa1"));
    }

    @Test
    void testBootstrapWithAdminPolicyOnly() throws PMException {
        PAP pap = new TestPAP();
        PDP pdp = new PDP(pap);

        pdp.bootstrap(new PolicyBootstrapper() {
            @Override
            public void bootstrap(PAP pap) throws PMException {
                long pc1 = pap.modify().graph().createPolicyClass("pc1");
                long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
                pap.modify().graph().createUser("u1", List.of(ua1));
            }
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
            pdp.bootstrap(new PolicyBootstrapper() {
                @Override
                public void bootstrap(PAP pap) throws PMException {

                }
            });
        });

        pap.reset();

        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read"));
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
            pdp.bootstrap(new PolicyBootstrapper() {
                @Override
                public void bootstrap(PAP pap) throws PMException {

                }
            });
        });

        pap.modify().obligations().createObligation(
            id("u1"),
            "obl1",
            new EventPattern(new SubjectPattern(), new AnyOperationPattern()),
            new ObligationResponse("evt", List.of())
        );

        assertThrows(BootstrapExistingPolicyException.class, () -> {
            pdp.bootstrap(new PolicyBootstrapper() {
                @Override
                public void bootstrap(PAP pap) throws PMException {

                }
            });
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
        pap.modify().graph().associate(ua1, AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("*"));

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
                set resource access rights ["read", "write"]
                
                resourceop read_file(@node("read") string name) {}
                resourceop write_file(@node("write") string name) {}
                
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["read"]
                
                create u "u1" in ["ua1"]
                create o "o1" in ["oa1"]
                """);

        PDP pdp = new PDP(pap);

        Object resp = pdp.adjudicateResourceOperation(new TestUserContext("u1"), "read_file",
            Map.of("name", "o1"));
        assertNull(resp);

        assertThrows(
            UnauthorizedException.class,
            () -> pdp.adjudicateResourceOperation(new TestUserContext("u1"), "write_file",
                Map.of("name", "o1"))
        );
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
                
                adminop op1() string {
                    check ["assign_to"] on ["oa2"]
                    create pc "test"
                    return "test"
                }
                """);

        PDP pdp = new PDP(pap);

        // custom operation
        Object resp = pdp.adjudicateAdminOperation(new TestUserContext("u1"),
            "op1",
            Map.of());
        assertEquals("test", resp);

        // denied
        assertThrows(UnauthorizedException.class, () -> pdp.adjudicateAdminOperation(new UserContext(id("u2")),
            "op1",
            Map.of()));
    }

    @Test
    void testAdjudicateDoesNotExist() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(new UserContext(0), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                
                set resource access rights ["read", "write"]
                
                resourceop read_file(@node("read") int64 id) {}

                """);
        PDP pdp = new PDP(pap);
        assertThrows(OperationDoesNotExistException.class,
                () -> pdp.adjudicateAdminOperation(new TestUserContext("u1"),
                    "op1",
                    Map.of()));
        assertThrows(OperationDoesNotExistException.class,
                () -> pdp.adjudicateResourceOperation(new TestUserContext("u1"), "write_file", Map.of()));
    }

    @Test
    void testAdjudicateAdminRoutine() throws PMException {
        PAP pap = new TestPAP();
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long ua2 = pap.modify().graph().createUserAttribute("ua2", List.of(pc1));
        pap.modify().graph().createUser("u1", List.of(ua1));
        pap.modify().graph().createUser("u2", List.of(ua2));
        pap.modify().graph().associate(ua1, AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("*"));

        FormalParameter<String> a = new FormalParameter<>("a", STRING_TYPE);

        pap.modify().operations().createAdminRoutine(new Routine<>("routine1", STRING_TYPE, List.of(a)) {
            @Override
            public String execute(PAP pap, Args args) throws PMException {
                pap.modify().graph().createPolicyClass(args.get(a));
                return "test1";
            }

        });
        pap.executePML(new TestUserContext("u1"), """
                routine routine2() map[string]string {
                    create PC "test2"
                    return {"test2": "test2"}
                }
                """);

        PDP pdp = new PDP(pap);

        Object response = pdp.adjudicateAdminRoutine(new TestUserContext("u1"),
            "routine1",
            Map.of(a.getName(), "test"));
        assertEquals("test1", response);
        response = pdp.adjudicateAdminRoutine(new TestUserContext("u1"),
            "routine2", Map.of());
        assertEquals(Map.of("test2", "test2"), response);

        assertTrue(pap.query().graph().nodeExists("test"));
        assertTrue(pap.query().graph().nodeExists("test2"));

        assertThrows(UnauthorizedException.class, () -> pdp.adjudicateAdminRoutine(new UserContext(id("u2")),
            "routine1",
            Map.of(a.getName(), "test3")));
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
        assertThrows(UnauthorizedException.class, () -> pdp.adjudicateAdminRoutine(new TestUserContext("u1"),
            "routine1", Map.of()));
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
            tx.executePML("r1()");
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
                associate "ua1" and PM_ADMIN_BASE_OA with ["*a"]
                
                create u "u1" in ["ua1"]
                
                adminop op1() {
                    create pc "pc2"
                    foreach x in ["ua2", "ua3"] {
                        create ua x in ["pc2"]
                    }
                }
                
                create obligation "o1"
                    when any user
                    performs create_user_attribute
                    do(ctx) {
                        create pc "test"
                    }
                """);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);
        pdp.adjudicateAdminOperation(new TestUserContext("u1"),
            "op1", Map.of());
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
                
                associate "ua1" and PM_ADMIN_BASE_OA with ["create_policy_class", "create_object_attribute"]
                
                create u "u1" in ["ua1"]
                
                adminop op1(string name) {
                    create pc name
                }
                
                create obligation "obl1"
                    when any user
                    performs op1
                    do(ctx) {
                        create oa "oa_" + ctx.args.name in [ctx.args.name]
                    }
                """);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        pdp.adjudicateAdminRoutine(new TestUserContext("u1"), List.of(
                new OperationRequest("op1", Map.of(NAME_PARAM.getName(), "pc2")),
                new OperationRequest("op1", Map.of(NAME_PARAM.getName(), "pc3"))
        ));

        assertTrue(pap.query().graph().nodeExists("pc2"));
        assertTrue(pap.query().graph().nodeExists("pc3"));
        assertTrue(pap.query().graph().nodeExists("oa_pc3"));
        assertTrue(pap.query().graph().nodeExists("oa_pc3"));
    }
}