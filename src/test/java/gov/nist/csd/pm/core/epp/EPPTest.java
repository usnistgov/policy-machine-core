package gov.nist.csd.pm.core.epp;

import static gov.nist.csd.pm.core.pap.PAPTest.ARG_A;
import static gov.nist.csd.pm.core.pap.PAPTest.ARG_B;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_OBJECT;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_OBJECT_ATTRIBUTE;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_OBLIGATION;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static gov.nist.csd.pm.core.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.operation.accessrights.AccessRightSet;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.modification.GraphModification;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.AnyOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.MatchesOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.subject.InSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.LogicalSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.ProcessSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.obligation.event.subject.UsernamePatternExpression;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.VoidType;
import gov.nist.csd.pm.core.pap.operation.graph.AssignOp;
import gov.nist.csd.pm.core.pap.pml.PMLCompiler;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.operation.admin.PMLAdminOperation;
import gov.nist.csd.pm.core.pap.pml.operation.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateNonPCStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import it.unimi.dsi.fastutil.longs.LongList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EPPTest {

    @Test
    void testCustomOperationEvent() throws PMException {
        MemoryPAP pap = new TestPAP();
        TestUserContext u1 = new TestUserContext("u1");
        pap.executePML(u1, """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                
                adminop op1(@node string a, @node string b) {
                
                }
                
                create obligation "obl1"
                    when any user
                    performs "op1" on (a, b) {
                        return a == "oa1" && b == "oa1"
                    }
                    do(ctx) {
                        create pc ctx.args.a + "pc1"
                        create pc ctx.args.b + "pc2"
                    }
                """);

        AdminOperation<String> op2 = new AdminOperation<>("op2", STRING_TYPE, List.of(ARG_A, ARG_B)) {

            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) {

            }

            @Override
            public String execute(PAP pap, Args args) throws PMException {
                return "";
            }
        };

        pap.modify().operations().createOperation(op2);

        pap.executePML(u1, """
            create obligation "obl2"
            when any user
            performs "op2" on (a, b) {
                return a == "oa2" && b == "oa2"
            }
            do(ctx) {
                create pc ctx.args.a + "pc3"
                create pc ctx.args.b + "pc4"
            }
            """);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        assertThrows(UnauthorizedException.class, () -> pdp.adjudicateOperation(
            u1,
            "op1",
            Map.of("a", "oa1",
                "b", "oa1")
        ));

        pap.modify().graph().associate(id("ua1"), AdminPolicyNode.PM_ADMIN_POLICY_CLASSES.nodeId(), new AccessRightSet("*a"));

        assertDoesNotThrow(() -> pdp.adjudicateOperation(
            u1,
            "op1",
            Map.of(
                "a", "oa1",
                "b", "oa1")
        ));

        assertDoesNotThrow(() -> pdp.adjudicateOperation(
            u1,
            "op2",
            Map.of(
                ARG_A.getName(), "oa2",
                ARG_B.getName(), "oa2"
            )
        ));

        assertTrue(pap.query().graph().nodeExists("oa1pc1"));
        assertTrue(pap.query().graph().nodeExists("oa1pc2"));
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
                
                set resource access rights ["read"]
                resourceop read_file(@node("read") string name)
                
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                
                associate "ua1" and "oa1" with ["read"]
                associate "ua1" and PM_ADMIN_POLICY_CLASSES with ["*a"]
                
                create obligation "obl1"
                    when any user
                    performs "read_file"
                    on (name) {
                        return name == "oa1"
                    }
                    do(ctx) {
                        create pc ctx.args.name + "pc1"
                    }
                """);
        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        assertDoesNotThrow(() -> pdp.adjudicateOperation(new UserContext(id("u1")), "read_file",
            Map.of("name", "oa1")));
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
                create obligation "test"
                    when any user
                    performs "create_object_attribute"
                    on (descendants) {
                        return contains(descendants, id("oa1"))
                    }
                    do(evtCtx) {
                        create PC "pc2"
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
                
                create obligation "test"
                    when any user
                    performs "create_object_attribute"
                    on (descendants) {
                        return contains(descendants, id("oa1"))
                    }
                    do(ctx) {
                        name := ctx.opName
                        create PC name

                        name = ctx.args.name
                        create PC name + "_test"
                        set properties of name + "_test" to {"key": name}

                        userCtx := ctx["user"]
                        create PC ctx["user"] + "_test"
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
                new EventPattern(new SubjectPattern(), new MatchesOperationPattern(CREATE_OBJECT_ATTRIBUTE)),
                new ObligationResponse("evtCtx", List.of(
                    new CreateNonPCStatement(
                        new StringLiteralExpression("o2"),
                        NodeType.O,
                        ArrayLiteralExpression.of(List.of(new StringLiteralExpression("oa1")), STRING_TYPE)
                    ),

                    // expect error for node already exists
                    new CreatePolicyClassStatement(new StringLiteralExpression("pc1"))
                ))
            );

            return null;
        });

        EventContext eventCtx = new EventContext(
            new EventContextUser("u1", null),
            CREATE_OBJECT_ATTRIBUTE,
            Map.of(
                "name", id("oa2"),
                "descendants", List.of(id("pc1"))
            )
        );

        assertThrows(PMException.class, () -> epp.processEvent(eventCtx));

        assertFalse(pap.query().graph().nodeExists("o2"));
        assertFalse(pap.query().graph().nodeExists("pc2"));
    }

    @Test
    void testCustomOperationInResponse() throws PMException {
        MemoryPAP pap = new TestPAP();

        PMLAdminOperation<?> pmlAdminOperation = new PMLAdminOperation<>("testFunc", new VoidType()) {

            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) {

            }

            @Override
            public Void execute(PAP pap, Args args) throws PMException {
                pap.modify().graph().createPolicyClass("test");

                return null;
            }

        };

        pap.modify().operations().createOperation(pmlAdminOperation);

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
                
                create obligation "test"
                    when any user
                    performs "create_object_attribute"
                    on (descendants) {
                        return contains(descendants, id("oa1"))
                    }
                    do(evtCtx) {
                        testFunc()
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
                
                create obligation "test"
                    when any user
                    performs "create_object_attribute"
                    on (descendants) {
                        return contains(descendants, id("oa1"))
                    }
                    do(evtCtx) {
                        if true {
                            return
                        }
                
                        create PC "test"
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
                
                adminop op1() {
                    check ["assign"] on ["oa1"]

                    create pc "test_pc"
                }
                
                routine routine1() {
                    create o "o1" in ["oa1"]
                }
                
                create obligation "obl1"
                    when any user
                    performs "create_policy_class"
                    do(ctx) {
                        op1()
                        routine1()
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

    private MemoryPAP testPAP() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read"));

        GraphModification graph = pap.modify().graph();

        long pc1 = graph.createPolicyClass("pc1");
        long ua1 = graph.createUserAttribute("ua1", LongList.of(pc1));
        graph.createUser("u1", LongList.of(ua1));

        return pap;
    }

    @Test
    void testUserAttributesMatches() throws PMException {
        EventPattern eventPattern = new EventPattern(
            new SubjectPattern(new InSubjectPatternExpression("ua2")),
            new AnyOperationPattern()
        );

        PAP pap = testPAP();
        pap.modify().graph().createUserAttribute("ua2", List.of(id("ua1")));
        pap.modify().graph().associate(id("ua2"), id("ua1"), new AccessRightSet("*"));

        UserContext userCtx = new UserContext(List.of(id("ua2")));

        EventContext eventContext = new EventContext(
            EventContextUser.fromUserContext(userCtx, pap),
            "assign",
            Map.of("ascendant", "a", "descendants", List.of("b"))
        );

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        boolean actual = pdp.runTx(userCtx, pdpTx -> epp.matches(userCtx, pdpTx, eventContext, eventPattern));
        assertTrue(actual);

    }

    @Test
    void testUserAttributesNotMatches() throws PMException {
        EventPattern eventPattern = new EventPattern(
            new SubjectPattern(new InSubjectPatternExpression("ua1")),
            new AnyOperationPattern()
        );

        PAP pap = testPAP();
        pap.modify().graph().createUserAttribute("ua2", List.of(id("ua1")));
        pap.modify().graph().associate(id("ua2"), id("ua1"), new AccessRightSet("*"));

        UserContext userCtx = new UserContext(List.of(id("ua2")));

        EventContext eventContext = new EventContext(
            EventContextUser.fromUserContext(userCtx, pap),
            "assign",
            Map.of("ascendant", "a", "descendants", List.of("b"))
        );

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        boolean actual = pdp.runTx(userCtx, pdpTx -> epp.matches(userCtx, pdpTx, eventContext, eventPattern));
        assertFalse(actual);
    }

    @Test
    void testOperationMatches() throws PMException {
        EventPattern eventPattern = new EventPattern(
            new SubjectPattern(),
            new AnyOperationPattern()
        );

        PAP pap = testPAP();

        UserContext userCtx = new UserContext(id("u1"));

        EventContext eventContext = new EventContext(
            EventContextUser.fromUserContext(userCtx, pap),
            "assign",
            Map.of("ascendant", "a", "descendants", List.of("b"))
        );

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        boolean actual = pdp.runTx(userCtx, pdpTx -> epp.matches(userCtx, pdpTx, eventContext, eventPattern));
        assertTrue(actual);
    }

    @Test
    void testOperationDoesNotMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
            new SubjectPattern(),
            new MatchesOperationPattern("op1")
        );

        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create u "u1" in ["ua1"]
            """);

        UserContext userCtx = new UserContext(id("u1"));

        EventContext eventContext = new EventContext(
            EventContextUser.fromUserContext(userCtx, pap),
            "assign",
            Map.of("ascendant", "a", "descendants", List.of("b"))
        );

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        boolean actual = pdp.runTx(userCtx, pdpTx -> epp.matches(userCtx, pdpTx, eventContext, eventPattern));
        assertFalse(actual);
    }

    @Test
    void testUserDoesNotMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
            new SubjectPattern(new UsernamePatternExpression("u2")),
            new AnyOperationPattern()
        );

        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create u "u1" in ["ua1"]
            """);

        UserContext userCtx = new UserContext(id("u1"), "");

        EventContext eventContext = new EventContext(
            EventContextUser.fromUserContext(userCtx, pap),
            "assign",
            Map.of("ascendant", "a", "descendants", List.of("b"))
        );

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        boolean actual = pdp.runTx(userCtx, pdpTx -> epp.matches(userCtx, pdpTx, eventContext, eventPattern));
        assertFalse(actual);
    }

    @Test
    void testUserAndProcessMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
            new SubjectPattern(new LogicalSubjectPatternExpression(
                new UsernamePatternExpression("u1"),
                new ProcessSubjectPatternExpression("p1"),
                false
            )),
            new AnyOperationPattern()
        );

        MemoryPAP pap = new TestPAP();

        EventContext eventContext = new EventContext(
            new EventContextUser("u1", "p1"),
            "assign",
            Map.of("ascendant", "a", "descendants", List.of("b"))
        );

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        UserContext userCtx = new UserContext(id("u1"), "");
        boolean actual = pdp.runTx(userCtx, pdpTx -> epp.matches(userCtx, pdpTx, eventContext, eventPattern));
        assertTrue(actual);
    }

    @Test
    void testUserMatchesProcessDoesNotMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
            new SubjectPattern(new LogicalSubjectPatternExpression(
                new UsernamePatternExpression("u1"),
                new ProcessSubjectPatternExpression("p1"),
                false
            )),
            new AnyOperationPattern()
        );

        MemoryPAP pap = new TestPAP();

        EventContext eventContext = new EventContext(
            new EventContextUser("u1", "p2"),
            "assign",
            Map.of("ascendant", "a", "descendants", List.of("b"))
        );

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        UserContext userCtx = new UserContext(id("u1"), "p2");
        boolean actual = pdp.runTx(userCtx, pdpTx -> epp.matches(userCtx, pdpTx, eventContext, eventPattern));
        assertTrue(actual);
    }

    @Test
    void testUserAndProcessDoNotMatch() throws PMException {
        EventPattern eventPattern = new EventPattern(
            new SubjectPattern(new LogicalSubjectPatternExpression(
                new UsernamePatternExpression("u2"),
                new ProcessSubjectPatternExpression("p1"),
                false
            )),
            new AnyOperationPattern()
        );

        MemoryPAP pap = new TestPAP();

        EventContext eventContext = new EventContext(
            new EventContextUser("u1", "p2"),
            "assign",
            Map.of("ascendant", id("a"), "descendants", List.of(id("b")))
        );

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        UserContext userCtx = new UserContext(id("u1"), "");
        boolean actual = pdp.runTx(userCtx, pdpTx -> epp.matches(userCtx, pdpTx, eventContext, eventPattern));
        assertFalse(actual);
    }

    @Test
    void testArgsMatch() throws PMException {
        CompileScope testCompileScope = new CompileScope(new MemoryPAP());
        testCompileScope.addVariable("ascendant", new Variable("ascendant", LONG_TYPE, false));
        EventPattern eventPattern = new EventPattern(
            new SubjectPattern(new UsernamePatternExpression("u1")),
            new MatchesOperationPattern(
                "assign",
                Set.of("ascendant"),
                new PMLStmtsRoutine<>(
                    "",
                    BOOLEAN_TYPE,
                    List.of(AssignOp.ASSIGN_ASCENDANT_PARAM, AssignOp.ASSIGN_DESCENDANTS_PARAM),
                    new PMLStatementBlock(new PMLCompiler().compilePML(new MemoryPAP(), testCompileScope, """
                        return name(ascendant) == "a"
                        """))
                )
            )
        );

        MemoryPAP pap = testPAP();
        pap.modify().graph().createObjectAttribute("oa1", List.of(id("pc1")));
        pap.modify().graph().createObjectAttribute("b", List.of(id("oa1")));
        pap.modify().graph().createObjectAttribute("a", List.of(id("oa1")));
        pap.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet("read"));

        UserContext userCtx = new UserContext(id("u1"), "");

        EventContext eventContext = new EventContext(
            EventContextUser.fromUserContext(userCtx, pap),
            "assign",
            Map.of("ascendant", id("a"), "descendants", List.of(id("b")))
        );

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        boolean actual = pdp.runTx(userCtx, pdpTx -> epp.matches(userCtx, pdpTx, eventContext, eventPattern));
        assertTrue(actual);
    }

    @Test
    void testArgsDoNotMatch() throws PMException {
        CompileScope testCompileScope = new CompileScope(new MemoryPAP());
        testCompileScope.addVariable("ascendant", new Variable("ascendant", LONG_TYPE, false));

        EventPattern eventPattern = new EventPattern(
            new SubjectPattern(new UsernamePatternExpression("u1")),
            new MatchesOperationPattern(
                "e1",
                Set.of("ascendant", "descendants"),
                new PMLStmtsRoutine<>(
                    "",
                    BOOLEAN_TYPE,
                    List.of(AssignOp.ASSIGN_ASCENDANT_PARAM, AssignOp.ASSIGN_DESCENDANTS_PARAM),
                    new PMLStatementBlock(new PMLCompiler().compilePML(new MemoryPAP(), testCompileScope, """
                        return name(ascendant) == "b"
                        """))
                )
            )
        );

        MemoryPAP pap = testPAP();

        UserContext userCtx = new UserContext(id("u1"), "");

        EventContext eventContext = new EventContext(
            EventContextUser.fromUserContext(userCtx, pap),
            "assign",
            Map.of("ascendant", "a", "descendants", List.of("b"))
        );

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        boolean actual = pdp.runTx(userCtx, pdpTx -> epp.matches(userCtx, pdpTx, eventContext, eventPattern));
        assertFalse(actual);
    }
}