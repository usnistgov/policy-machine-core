package gov.nist.csd.pm.core.pap.pml;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.Routine;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnNode;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class PMLTest {

    private static final FormalParameter<String> ARGA = new FormalParameter<>("a", STRING_TYPE);
    private static final FormalParameter<List<String>> ARGB = new FormalParameter<>("b", ListType.of(STRING_TYPE));
    private static final FormalParameter<Map<String, String>> ARGC = new FormalParameter<>("c", MapType.of(STRING_TYPE, STRING_TYPE));

    @Test
    void testCallingNonPMLOperationAndRoutineFromPMLWithArgsAndReturnValue() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua1"]
                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:graph:assignment:ascendant:create"]
                
                create conj node prohibition "pro1"
                deny "u2"
                arset ["admin:graph:assignment:ascendant:create"]
                include [PM_ADMIN_BASE_OA]
                """);

        AdminOperation<?> op1 = new AdminOperation<>(
            "op1",
            VOID_TYPE,
            List.of(ARGA, ARGB, ARGC),
            new RequiredCapability(
                new RequiredPrivilegeOnNode(
                    AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName(),
                    new AccessRightSet("admin:graph:assignment:ascendant:create")
                )
            )
        ) {
            @Override
            public Void execute(PAP pap, Args actualArgs) throws PMException {
                String a = actualArgs.get(ARGA);
                List<String> b = actualArgs.get(ARGB);
                Map<String, String> c = actualArgs.get(ARGC);

                pap.modify().graph().createPolicyClass("1" + a);

                for (String b1 : b) {
                    pap.modify().graph().createPolicyClass("1" + b1);
                }

                for (Map.Entry<String, String> c1 : c.entrySet()) {
                    pap.modify().graph().createPolicyClass("1" + c1.getKey());
                    pap.modify().graph().createPolicyClass("1" + c1.getValue());
                }

                return null;
            }

        };
        pap.modify().operations().createOperation(op1);

        pap.modify().operations().createOperation(new Routine<>("routine1", VOID_TYPE, List.of(ARGA, ARGB, ARGC)) {
            @Override
            public Void execute(PAP pap, Args args) throws PMException {
                pap.executeOperation(op1, args);

                return null;
            }

        });

        PDP pdp = new PDP(pap);
        pdp.executePML(new TestUserContext("u1"), """
                op1("a", ["b", "c"], {"d": "e", "f": "g"})
                """);
        assertTrue(pap.query().graph().nodeExists("1a"));
        assertTrue(pap.query().graph().nodeExists("1b"));
        assertTrue(pap.query().graph().nodeExists("1c"));
        assertTrue(pap.query().graph().nodeExists("1d"));
        assertTrue(pap.query().graph().nodeExists("1e"));
        assertTrue(pap.query().graph().nodeExists("1f"));
        assertTrue(pap.query().graph().nodeExists("1g"));

        assertThrows(UnauthorizedException.class, () -> pdp.executePML(new UserContext(id("u2")), """
                op1("a", ["b", "c"], {"d": "e", "f": "g"})
                """));

        pdp.executePML(new TestUserContext("u1"), """
                routine1("1", ["2", "3"], {"4": "5", "6": "7"})
                """);
        assertTrue(pap.query().graph().nodeExists("11"));
        assertTrue(pap.query().graph().nodeExists("12"));
        assertTrue(pap.query().graph().nodeExists("13"));
        assertTrue(pap.query().graph().nodeExists("14"));
        assertTrue(pap.query().graph().nodeExists("15"));
        assertTrue(pap.query().graph().nodeExists("16"));
        assertTrue(pap.query().graph().nodeExists("17"));

        assertThrows(UnauthorizedException.class, () -> pdp.executePML(new UserContext(id("u2")), """
                routine1("1", ["2", "3"], {"4": "5", "6": "7"})
                """));
    }

    @Test
    void testCallPMLOperationAndRoutineFromNonPML() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua1"]
                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:graph:assignment:ascendant:create"]
                
                create conj node prohibition "pro1"
                deny "u2"
                arset ["admin:graph:assignment:ascendant:create"]
                include [PM_ADMIN_BASE_OA]
                
                @reqcap({
                    require ["admin:graph:assignment:ascendant:create"] on [PM_ADMIN_BASE_OA]
                })
                adminop op1(string a, []string b, map[string]string c) {
                    create pc "1" + a
                
                    foreach x in b {
                        create pc "1" + x
                    }
                
                    foreach x, y in c {
                        create pc "1" + x
                        create pc "1" + y
                    }
                }
                
                routine routine1(string a, []string b, map[string]string c) {
                    op1(a, b, c)
                }
                """);

        PDP pdp = new PDP(pap);
        assertDoesNotThrow(() -> pdp.adjudicateOperation(
            new TestUserContext("u1"),
            "op1",
            Map.of(
                ARGA.getName(), "a",
                ARGB.getName(), List.of("b", "c"),
                ARGC.getName(), Map.of("d", "e", "f", "g")
            )
        ));
        assertTrue(pap.query().graph().nodeExists("1a"));
        assertTrue(pap.query().graph().nodeExists("1b"));
        assertTrue(pap.query().graph().nodeExists("1c"));
        assertTrue(pap.query().graph().nodeExists("1d"));
        assertTrue(pap.query().graph().nodeExists("1e"));
        assertTrue(pap.query().graph().nodeExists("1f"));
        assertTrue(pap.query().graph().nodeExists("1g"));

        assertThrows(UnauthorizedException.class, () -> pdp.adjudicateOperation(new UserContext(id("u2")),
            "op1",
            Map.of(
                ARGA.getName(), "a",
                ARGB.getName(), List.of("b", "c"),
                ARGC.getName(), Map.of("d", "e", "f", "g")
            )
        ));

        assertDoesNotThrow(() -> pdp.adjudicateOperation(new TestUserContext("u1"),
            "op1",
            Map.of(
                ARGA.getName(), "1",
                ARGB.getName(), List.of("2", "3"),
                ARGC.getName(), Map.of("4", "5", "6", "7")
            )
        ));
        assertTrue(pap.query().graph().nodeExists("11"));
        assertTrue(pap.query().graph().nodeExists("12"));
        assertTrue(pap.query().graph().nodeExists("13"));
        assertTrue(pap.query().graph().nodeExists("14"));
        assertTrue(pap.query().graph().nodeExists("15"));
        assertTrue(pap.query().graph().nodeExists("16"));
        assertTrue(pap.query().graph().nodeExists("17"));

        assertThrows(UnauthorizedException.class, () -> pdp.adjudicateOperation(new UserContext(id("u2")), "op1",
            Map.of(
                ARGA.getName(), "1",
                ARGB.getName(), List.of("2", "3"),
                ARGC.getName(), Map.of("4", "5", "6", "7")
            )
        ));
    }

    @Test
    void testEmptyValueIsCorrectlyAssignedTheOperationReturnType() throws PMException {
        String pml = """
            adminop op1() map[string]string {
                return {}
            }
            
            adminop op2(map[string]string a) {}
            
            adminop op3() []string {
                return []
            }
            
            adminop op4([]string a) {}
            
            op2(op1())
            op4(op3())
            """;
        MemoryPAP pap = new MemoryPAP();
        assertDoesNotThrow(() -> pap.executePML(new UserContext(-1), pml));

        String pml2 = """
            adminop op1() map[string]string {
                return {}
            }
            
            adminop op2(string a) {}
            
            op2(op1())
            """;
        MemoryPAP pap2 = new MemoryPAP();
        assertThrows(PMLCompilationException.class, () -> pap2.executePML(new UserContext(-1), pml2));
    }

    @Test
    void testReassignmentOfVariableType() throws PMException {
        String pml = """
            adminop op1() map[string]string {
                return {}
            }
            
            adminop op2(string s) {}
            
            a := op1()
            a = "test"
            op2(a)
            
            """;
        MemoryPAP pap = new MemoryPAP();
        assertDoesNotThrow(() -> pap.executePML(new UserContext(-1), pml));
    }

    @Test
    void testReturnValueFromRootPMLInIfStatement() throws PMException {
        String pml = """
            adminop op1(string a) string {
                return a
            }
            
            if op1("a") == "a" {
                return "a"
            }
            
            return "b"
            """;
        MemoryPAP pap = new MemoryPAP();
        Object o = pap.executePML(new UserContext(-1), pml);
        assertEquals(o, "a");
    }

    @Test
    void testObligationMatchingFuncHasBasicAndQueryOperationsOnly() throws PMException {
        String pml = """
            adminop test() string { return "test" }
            
            create obligation "o1"
            when any user
            performs "test" on () {
                a := getAdjacentAscendants("123")
                b := test()
            }
            do(ctx) {
                create pc "pc1"
            }
            """;
        MemoryPAP pap = new MemoryPAP();
        PMLCompilationException e = assertThrows(
            PMLCompilationException.class,
            () -> pap.executePML(new UserContext(-1), pml)
        );
        assertEquals(
            "unknown operation 'test' in scope",
            e.getErrors().getFirst().errorMessage()
        );
    }

    @Test
    void testQueryDefinitionStmt() throws PMException {
        String pml = """
            create pc "123"
            
            adminop test() string { return "test" }
            
            query q1() string {
                n := getNode("123")
                return test()
            }
            """;
        MemoryPAP pap = new MemoryPAP();
        PMLCompilationException e = assertThrows(
            PMLCompilationException.class,
            () -> pap.executePML(new UserContext(-1), pml)
        );
        assertEquals(
            "unknown operation 'test' in scope",
            e.getErrors().getFirst().errorMessage()
        );
    }

    @Test
    void testPMLBlockReturnWithDifferentReturnTypes() throws PMException {
        String pml = """
            if nodeExists("a") {
                return "a"
            }
            
            return true
            """;
        MemoryPAP pap = new MemoryPAP();
        Object o = assertDoesNotThrow(() -> pap.executePML(new UserContext(-1), pml));
        assertEquals(o, true);

        pap.modify().graph().createPolicyClass("a");
        o = assertDoesNotThrow(() -> pap.executePML(new UserContext(-1), pml));
        assertEquals(o, "a");
    }

    @Test
    void testIndexExpressionAsStatementCausesCompilationError() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext(-1), """
                create pc "pc1"
                """);

        assertDoesNotThrow(() -> pap.executePML(new UserContext(-1), """
                getNode("pc1")
                """));

        assertThrows(PMLCompilationException.class, () -> pap.executePML(new UserContext(-1), """
                getNode("pc1").name
                """));
    }

    @Test
    void testUsingKeywordsAsMapKeys() {
        String pml = """
                m := {}
                x := [
                    m.test,
                    m.operation,
                    m.check,
                    m.routine,
                    m.function,
                    m.create,
                    m.delete,
                    m.rule,
                    m.when,
                    m.performs,
                    m.on,
                    m.in,
                    m.do,
                    m.any,
                    m.intersection,
                    m.union,
                    m.process,
                    m.assign,
                    m.deassign,
                    m.from,
                    m.of,
                    m.to,
                    m.associate,
                    m.and,
                    m.with,
                    m.dissociate,
                    m.deny,
                    m.prohibition,
                    m.obligation,
                    m.node,
                    m.user,
                    m.pc,
                    m.oa,
                    m.ua,
                    m.o,
                    m.u,
                    m.break,
                    m.default,
                    m.map,
                    m.else,
                    m.const,
                    m.if,
                    m.range,
                    m.continue,
                    m.foreach,
                    m.return,
                    m.var,
                    m.string,
                    m.bool,
                    m.void,
                    m.array,
                    m.nil,
                    m.true,
                    m.false,
                    m.adminop,
                    m.resourceop,
                    m.query
                ]
                """;

        PMLCompiler pmlCompiler = new PMLCompiler();
        assertDoesNotThrow(() -> pmlCompiler.compilePML(new MemoryPAP(), pml));
    }

    @Test
    void testReqCap() throws PMException {
        String pml = """
            set resource access rights ["read"]
            
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create u "u1" in ["ua1"]
            create oa "oa1" in ["pc1"]
            create o "o1" in ["oa1"]
            create oa "oa2" in ["pc1"]
            create o "o2" in ["oa2"]
            
            associate "ua1" to "oa1" with ["read"]
            associate "ua1" to "oa2" with ["read"]
            
            @reqcap({
                require ["read"] on [file]
            })
            @reqcap({
                require ["read"] on ["o2"]
            })
            resourceop read_file(@node string file) { }
            """;
        PAP pap = new TestPAP();
        pap.executePML(null, pml);
        Operation<?> readFile = pap.query().operations().getOperation("read_file");

        assertDoesNotThrow(() -> readFile.canExecute(pap, new UserContext(id("u1")), new Args().put(new NodeNameFormalParameter("file"), "o1")));

        pap.modify().graph().dissociate(id("ua1"), id("oa1"));

        assertDoesNotThrow(() -> readFile.canExecute(pap, new UserContext(id("u1")), new Args().put(new NodeNameFormalParameter("file"), "o1")));
    }
}
