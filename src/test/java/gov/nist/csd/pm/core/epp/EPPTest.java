package gov.nist.csd.pm.core.epp;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.type.VoidType;
import gov.nist.csd.pm.core.pap.obligation.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.Response;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLOperation;
import gov.nist.csd.pm.core.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateNonPCStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import gov.nist.csd.pm.core.pap.pml.statement.result.VoidResult;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.pdp.adjudication.Decision;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.Objects;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.*;
import static gov.nist.csd.pm.core.pap.PAPTest.ARG_A;
import static gov.nist.csd.pm.core.pap.PAPTest.ARG_B;
import static gov.nist.csd.pm.core.pap.function.arg.type.Type.STRING_TYPE;
import static gov.nist.csd.pm.core.pdp.adjudication.Decision.GRANT;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static gov.nist.csd.pm.core.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.*;

class EPPTest {

    @Test
    void testCustomOperationEvent() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                
                operation op1(@node string a, @node []string b) {
                
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
                        create pc ctx.args.a + "pc1"
                
                        foreach x in ctx.args.b {
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
                        create pc ctx.args.a + "pc3"
                
                        foreach x in ctx.args.b {
                            create pc x + "pc4"
                        }
                    }
                }
                """);

        Operation<String, Args> op2 = new Operation<>("op2", List.of(ARG_A, ARG_B)) {

            @Override
            protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
                return new Args(argsMap);
            }

            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) {

            }

            @Override
            public String execute(PAP pap, Args args) {
                return "";
            }
        };

        pap.modify().operations().createAdminOperation(op2);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        assertThrows(UnauthorizedException.class, () -> pdp.adjudicateAdminOperation(
            new TestUserContext("u1"),
            "op1",
            Map.of("a", "oa1",
                "b", List.of("oa1", "oa2"))
        ));

        pap.modify().graph().associate(id("ua1"), AdminPolicyNode.PM_ADMIN_POLICY_CLASSES.nodeId(), new AccessRightSet("*a"));

        assertDoesNotThrow(() -> pdp.adjudicateAdminOperation(
            new TestUserContext("u1"),
            "op1",
            Map.of(
                "a", "oa1",
                "b", List.of("oa1", "oa2"))
        ));

        assertDoesNotThrow(() -> pdp.adjudicateAdminOperation(
            new TestUserContext("u1"),
            "op2",
            Map.of(
                ARG_A.getName(), "oa2",
                ARG_B.getName(), List.of("oa2")
            )
        ));

        assertTrue(pap.query().graph().nodeExists("oa1pc1"));
        assertTrue(pap.query().graph().nodeExists("oa1pc2"));
        assertTrue(pap.query().graph().nodeExists("oa2pc2"));
        assertTrue(pap.query().graph().nodeExists("oa2pc3"));
        assertTrue(pap.query().graph().nodeExists("oa2pc4"));
    }

    @Test
    void testResourceOperationEvent() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                
                set resource operations ["read"]
                
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                
                associate "ua1" and "oa1" with ["read"]
                associate "ua1" and PM_ADMIN_POLICY_CLASSES with ["*a"]
                
                create obligation "obl1" {
                    create rule "op1"
                    when any user
                    performs "read"
                    on {
                        target: "oa1"
                    }
                    do(ctx) {
                        create pc ctx.args.target + "pc1"
                    }
                }
                """);
        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        assertDoesNotThrow(() -> pdp.adjudicateResourceOperation(new UserContext(id("u1")), id("oa1"), "read"));
        assertTrue(pap.query().graph().nodeExists("oa1pc1"));
    }

    @Test
    void test() throws PMException {
        MemoryPAP pap = new TestPAP();
        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        String pml = """
                create pc "pc1"
                create oa "oa1" in ["pc1"]
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                associate "ua1" and "oa1" with ["*"]
                associate "ua1" and PM_ADMIN_POLICY_CLASSES with ["*"]
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
        pap.executePML(new TestUserContext("u1"), pml);

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("oa1"));

        pdp.runTx(new UserContext(id("u1")), (txPDP) -> txPDP.modify().graph().createObjectAttribute("oa2", ids("oa1")));

        assertTrue(pap.query().graph().nodeExists("pc2"));

    }

    @Test
    void testAccessingEventContextInResponse() throws PMException {
        MemoryPAP pap = new TestPAP();
        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        String pml = """                
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create oa "oa1" in ["pc1"]
                
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and PM_ADMIN_BASE_OA with ["*a"]
                
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

                        name = ctx.args.name
                        create policy class name + "_test"
                        set properties of name + "_test" to {"key": name}

                        userCtx := ctx["user"]
                        create policy class ctx["user"] + "_test"
                    }
                }
                """;
        pap.executePML(new TestUserContext("u1"), pml);

        pdp.runTx(new UserContext(id("u1")), (txPDP) -> txPDP.modify().graph().createObjectAttribute("oa2",
            ids("oa1")));

        assertTrue(pap.query().graph().getPolicyClasses().containsAll(ids(
            "pc1", "create_object_attribute", "oa2_test", "u1_test"
        )));
    }

    @Test
    void testErrorInEPPResponse() throws PMException {
        MemoryPAP pap = new TestPAP();
        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        pap.runTx((txPAP) -> {
            txPAP.modify().graph().createPolicyClass("pc1");
            txPAP.modify().graph().createUserAttribute("ua1", ids("pc1"));
            txPAP.modify().graph().createUserAttribute("ua2", ids("pc1"));
            txPAP.modify().graph().associate(id("ua2"), id("ua1"), new AccessRightSet("*"));
            txPAP.modify().graph().associate(id("ua2"), AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("*"));
            txPAP.modify().graph().createObjectAttribute("oa1",  ids("pc1"));
            txPAP.modify().graph().createUser("u1",  ids("ua1", "ua2"));
            txPAP.modify().graph().createObject("o1",  ids("oa1"));
            txPAP.modify().graph().associate(id("ua1"), AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(),
                new AccessRightSet(CREATE_OBLIGATION));
            txPAP.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet(CREATE_OBJECT));
            txPAP.modify().graph().associate(id("ua1"), AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("*"));
        });

        pdp.runTx(new UserContext(id("u1")), (policy) -> {
            policy.modify().obligations().createObligation(id("u1"), "test",
                List.of(new Rule(
                    "rule1",
                    new EventPattern(new SubjectPattern(), new OperationPattern(CREATE_OBJECT_ATTRIBUTE)),
                    new Response("evtCtx", List.of(
                        new CreateNonPCStatement(
                            new StringLiteralExpression("o2"),
                            NodeType.O,
                            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("oa1")), STRING_TYPE)
                        ),

                        // expect error for node already exists
                        new CreatePolicyClassStatement(new StringLiteralExpression("pc1"))
                    ))
                ))
            );

            return null;
        });

        EventContext eventCtx = new EventContext(
            "u1",
            null,
            CREATE_OBJECT_ATTRIBUTE,
            Map.of(
                "name", "oa2",
                "descendants", List.of("pc1")
            )
        );

        assertThrows(PMException.class, () -> epp.processEvent(eventCtx));

        assertFalse(pap.query().graph().nodeExists("o2"));
        assertFalse(pap.query().graph().nodeExists("pc2"));
    }

    @Test
    void testCustomFunctionInResponse() throws PMException {
        MemoryPAP pap = new TestPAP();

        PMLOperation pmlOperation = new PMLOperation("testFunc", new VoidType()) {

            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) {

            }

            @Override
            public Object execute(PAP pap, Args args) throws PMException {
                pap.modify().graph().createPolicyClass("test");

                return new VoidResult();
            }

            @Override
            protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
                return null;
            }
        };

        pap.modify().operations().createAdminOperation(pmlOperation);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        String pml = """                
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create oa "oa1" in ["pc1"]
                
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and PM_ADMIN_POLICY_CLASSES with ["create_policy_class"]
                
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
        pap.executePML(new TestUserContext("u1"), pml);

        pdp.runTx(new UserContext(id("u1")), (txPDP) -> {
            txPDP.modify().graph().createObjectAttribute("oa2", ids("oa1"));

            return null;
        });

        assertTrue(pap.query().graph().nodeExists("test"));
    }

    @Test
    void testReturnInResponse() throws PMException {
        MemoryPAP pap = new TestPAP();

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        String pml = """                
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create oa "oa1" in ["pc1"]
                
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and PM_ADMIN_POLICY_CLASSES with ["create_policy_class"]
                
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
        pap.executePML(new TestUserContext("u1"), pml);

        pdp.runTx(new UserContext(id("u1")), (txPDP) -> txPDP.modify().graph().createObjectAttribute("oa2", ids("oa1")));
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
                associate "ua1" and PM_ADMIN_POLICY_CLASSES with ["*a"]
                
                operation op1() {
                    check "assign" on ["oa1"]
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
        MemoryPAP pap = new TestPAP();
        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);
        pap.executePML(new TestUserContext("u1"), pml);
        pdp.runTx(new TestUserContext("u1"), (txPDP) -> txPDP.modify().graph().createPolicyClass("u1_pc"));
        assertTrue(pap.query().graph().nodeExists("test_pc"));
        assertTrue(pap.query().graph().nodeExists("o1"));
        assertTrue(pap.query().graph().nodeExists("u1_pc"));

        // as u2 fail
        pap.reset();
        PDP pdp2 = new PDP(pap);
        epp = new EPP(pdp2, pap);
        epp.subscribeTo(pdp2);

        pap.executePML(new TestUserContext("u2"), pml);
        assertThrows(UnauthorizedException.class, () -> pdp2.runTx(new UserContext(id("u2")), (txPDP) -> txPDP.modify().graph().createPolicyClass("u2_pc")));
        assertFalse(pap.query().graph().nodeExists("test_pc"));
        assertFalse(pap.query().graph().nodeExists("o1"));
        assertFalse(pap.query().graph().nodeExists("u1_pc"));
    }
}