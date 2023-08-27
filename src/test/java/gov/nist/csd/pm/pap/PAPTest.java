package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.SamplePolicy;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.mysql.MysqlPolicyStore;
import gov.nist.csd.pm.pap.mysql.MysqlTestEnv;
import gov.nist.csd.pm.policy.PolicyEquals;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static gov.nist.csd.pm.pap.SuperUserBootstrapper.SUPER_USER;
import static org.junit.jupiter.api.Assertions.*;

class PAPTest {

    private PAP pap;

    public PAPTest(PAP pap) {
        this.pap = pap;
    }

    public void runTests() {
        // run all tests with pap field
        /*
        in memory pap test
        new PAPTest(new PAP(new MemoryPolicyStore()))
        papTest.run();
         */
    }

    private static MysqlTestEnv testEnv;

    static void runTest(TestRunner testRunner) throws PMException {
        testRunner.run(new PAP(new MemoryPolicyStore()));

        try (Connection connection = testEnv.getConnection()) {
            PAP mysqlPAP = new PAP(new MysqlPolicyStore(connection));
            testRunner.run(mysqlPAP);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    interface TestRunner {
        void run(PAP pap) throws PMException;
    }

    @BeforeAll
    public static void start() throws IOException, PMException {
        testEnv = new MysqlTestEnv();
        testEnv.start();
    }

    @AfterAll
    public static void stop() {
        testEnv.stop();
    }

    @AfterEach
    void reset() throws SQLException {
        testEnv.reset();
    }






    @Test
    void testTx() throws PMException {
        runTest(pap -> {
            pap.beginTx();
            pap.graph().createPolicyClass("pc1");
            pap.graph().createObjectAttribute("oa1", "pc1");
            pap.graph().createUserAttribute("ua1", "pc1");
            pap.graph().associate("ua1", "oa1", new AccessRightSet());
            pap.commit();

            assertTrue(pap.graph().nodeExists("pc1"));
            assertTrue(pap.graph().nodeExists("oa1"));
            assertTrue(pap.graph().nodeExists("ua1"));
            assertTrue(pap.graph().getAssociationsWithSource("ua1").contains(new Association("ua1", "oa1")));

            pap.beginTx();
            pap.graph().deleteNode("ua1");
            pap.rollback();
            assertTrue(pap.graph().nodeExists("ua1"));
        });
    }

    private static final String input = """
            set resource access rights ['read', 'write', 'execute']
            create policy class 'pc1'
            set properties of 'pc1' to {'k':'v'}
            create oa 'oa1' in ['pc1']
            set properties of 'oa1' to {'k1':'v1'}
            create ua 'ua1' in ['pc1']
            associate 'ua1' and 'oa1' with ['read', 'write']
            create prohibition 'p1' deny user attribute 'ua1' access rights ['read'] on union of [!'oa1']
            create obligation 'obl1' {
                create rule 'rule1'
                when any user
                performs ['event1', 'event2']
                do(evtCtx) {
                    let event = evtCtx['event']
                    if equals(event, 'event1') {
                        create policy class 'e1'
                    } else if equals(event, 'event2') {
                        create policy class 'e2'
                    }
                }
            }
            const testConst = "hello world"
            function testFunc() void {
                create pc "pc1"
            }
            """;
    private static final String expected = """
            const testConst = 'hello world'
            function testFunc() void {create policy class 'pc1'}
            set resource access rights ['read', 'write', 'execute']
            create policy class 'super_policy'
            create user attribute 'super_ua' in ['super_policy']
            create user attribute 'super_ua1' in ['super_policy']
            associate 'super_ua1' and 'super_ua' with ['*']
            create object attribute 'super_oa' in ['super_policy']
            associate 'super_ua' and 'super_oa' with ['*']
            create user 'super' in ['super_ua']
            assign 'super' to ['super_ua1']
            create object attribute 'super_policy_pc_rep' in ['super_oa']
            create object attribute 'pc1_pc_rep' in ['super_oa']
            create policy class 'pc1'
            set properties of 'pc1' to {'k': 'v'}
            create object attribute 'oa1' in ['pc1']
            set properties of 'oa1' to {'k1': 'v1'}
            associate 'super_ua' and 'oa1' with ['*']
            create user attribute 'ua1' in ['pc1']
            associate 'super_ua' and 'ua1' with ['*']
            associate 'ua1' and 'oa1' with ['read', 'write']
            create prohibition 'p1' deny user attribute 'ua1' access rights ['read'] on union of [!'oa1']
            create obligation 'obl1' {create rule 'rule1' when any user performs ['event1', 'event2'] on any policy element do (evtCtx) {let event = evtCtx['event']if equals(event, 'event1') {create policy class 'e1'} else if equals(event, 'event2') {create policy class 'e2'} }}
            """.trim();
    @Test
    void testSerialize() throws PMException {
        runTest(pap -> {
            pap.deserialize().fromPML(new UserContext(SUPER_USER), input);
            String actual = pap.serialize().toPML();
            assertEquals(new ArrayList<>(), pmlEqual(expected, actual));

            pap.deserialize().fromPML(new UserContext(SUPER_USER), actual);
            actual = pap.serialize().toPML();
            assertEquals(new ArrayList<>(), pmlEqual(expected, actual));

            String json = pap.serialize().toJSON();
            MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
            memoryPolicyStore.deserialize().fromJSON(json);
            pap.deserialize().fromJSON(json);
            PolicyEquals.check(pap, memoryPolicyStore);
        });
    }

    private List<String> pmlEqual(String expected, String actual) {
        List<String> expectedLines = sortLines(expected);
        List<String> actualLines = sortLines(actual);
        expectedLines.removeIf(line -> actualLines.contains(line));
        return expectedLines;
    }

    private List<String> sortLines(String pml) {
        List<String> lines = new ArrayList<>();
        Scanner sc = new Scanner(pml);
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }

        Collections.sort(lines);
        return lines;
    }

    @Test
    void testExecutePML() throws PMException {
        runTest(pap -> {
            try {
                SamplePolicy.loadSamplePolicyFromPML(pap);

                FunctionDefinitionStatement functionDefinitionStatement = new FunctionDefinitionStatement(
                        "testfunc",
                        Type.voidType(),
                        List.of(),
                        (ctx, policy) -> {
                            policy.graph().createPolicyClass("pc3");
                            return new Value();
                        }
                );

                pap.executePML(new UserContext(SUPER_USER), "create ua 'ua3' in ['pc2']\ntestfunc()", functionDefinitionStatement);
                assertTrue(pap.graph().nodeExists("ua3"));
                assertTrue(pap.graph().nodeExists("pc3"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testAssignAll() throws PMException {
        runTest(pap -> {
            String pml = """
                    create pc 'pc1'
                    create oa 'oa1' in ['pc1']
                    create oa 'oa2' in ['pc1']
                    create ua 'ua1' in ['pc1']
                    
                    for i in range [1, 10] {
                        let name = concat(["o", numToStr(i)])
                        create object name in ['oa1']
                    }
                    """;
            pap.deserialize().fromPML(new UserContext(SUPER_USER), pml);

            List<String> children = pap.graph().getChildren("oa1");
            pap.graph().assignAll(children, "oa2");

            assertEquals(10, pap.graph().getChildren("oa2").size());

            assertDoesNotThrow(() -> {
                pap.graph().assignAll(children, "oa2");
            });

            // reset policy
            pap.deserialize().fromPML(new UserContext(SUPER_USER), pml);

            // test with illegal assignment
            children.add("ua1");
            assertThrows(PMException.class, () -> {
                pap.graph().assignAll(children, "oa2");
            });
            assertTrue(pap.graph().getChildren("oa2").isEmpty());

            // test non existing target
            assertThrows(PMException.class, () -> {
                pap.graph().assignAll(children, "oa3");
            });

            // test non existing child
            assertThrows(PMException.class, () -> {
                children.remove("ua1");
                children.add("oDNE");
                pap.graph().assignAll(children, "oa2");
            });
        });
    }

    @Test
    void testDeassignAll() throws PMException {
        runTest(pap -> {
            String pml = """
                    create pc 'pc1'
                    create oa 'oa1' in ['pc1']
                    create oa 'oa2' in ['pc1']
                    create ua 'ua1' in ['pc1']
                    
                    for i in range [1, 10] {
                        let name = concat(["o", numToStr(i)])
                        create object name in ['oa1']
                    }
                    
                    for i in range [1, 5] {
                        let name = concat(["o", numToStr(i)])
                        assign name to ['oa2']
                    }
                    """;
            pap.deserialize().fromPML(new UserContext(SUPER_USER), pml);

            List<String> toDelete = new ArrayList<>(List.of("o1", "o2", "o3", "o4", "o5"));
            pap.graph().deassignAll(toDelete, "oa1");
            assertEquals(5, pap.graph().getChildren("oa1").size());

            toDelete.clear();
            toDelete.add("oDNE");
            assertThrows(PMException.class, () -> {
                pap.graph().deassignAll(toDelete, "oa2");
            });

            toDelete.clear();
            toDelete.add("o9");
            assertDoesNotThrow(() -> {
                pap.graph().deassignAll(toDelete, "oa2");
            });
        });
    }

    @Test
    void testDeassignAllAndDelete() throws PMException {
        runTest(pap -> {
            String pml = """
                    create pc 'pc1'
                    create oa 'oa1' in ['pc1']
                    create oa 'oa2' in ['pc1']
                    create ua 'ua1' in ['pc1']
                    
                    for i in range [1, 10] {
                        let name = concat(["o", numToStr(i)])
                        create object name in ['oa1']
                    }
                    
                    for i in range [1, 5] {
                        let name = concat(["o", numToStr(i)])
                        assign name to ['oa2']
                    }
                    """;
            pap.deserialize().fromPML(new UserContext(SUPER_USER), pml);

            assertThrows(PMException.class, () -> {
                pap.graph().deassignAllFromAndDelete("oa1");
            });

            pap.graph().assignAll(pap.graph().getChildren("oa1"), "oa2");

            pap.graph().deassignAllFromAndDelete("oa1");
            assertFalse(pap.graph().nodeExists("oa1"));
        });
    }
}