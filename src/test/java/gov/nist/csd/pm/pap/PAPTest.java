package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.pap.mysql.MysqlPAP;
import gov.nist.csd.pm.pap.mysql.MysqlTestEnv;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.author.pal.statement.CreateAttrStatement;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.author.pal.model.expression.Literal;
import gov.nist.csd.pm.policy.author.pal.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.Properties;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssignmentException;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssociationException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.pap.naming.Naming;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.toProperties;
import static gov.nist.csd.pm.pap.policies.SuperPolicy.*;
import static org.junit.jupiter.api.Assertions.*;

class PAPTest {

    private static MysqlTestEnv testEnv;

    public void runTest(TestRunner testRunner) throws PMException {
        testRunner.run(new MemoryPAP());

        try (Connection connection = testEnv.getConnection()) {
            PAP mysqlPAP = new MysqlPAP(connection);
            testRunner.run(mysqlPAP);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public interface TestRunner {
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
    public void testSuperPolicy() throws PMException {
        runTest(pap -> {
            assertTrue(pap.graph().nodeExists(SUPER_PC));

            String baseOA = Naming.baseObjectAttribute(SUPER_PC);
            String baseUA = Naming.baseUserAttribute(SUPER_PC);
            assertTrue(pap.graph().nodeExists(baseOA));
            assertTrue(pap.graph().nodeExists(baseUA));

            assertTrue(pap.graph().nodeExists(SUPER_UA));
            assertTrue(pap.graph().nodeExists(SUPER_USER));
            assertTrue(pap.graph().nodeExists(SUPER_OA));
            assertTrue(pap.graph().nodeExists(SUPER_OBJECT));

            assertTrue(pap.graph().getChildren(SUPER_PC).containsAll(Arrays.asList(SUPER_UA, baseUA, baseOA)));

            assertTrue(pap.graph().getChildren(baseUA).contains(SUPER_USER));
            assertTrue(pap.graph().getParents(baseUA).contains(SUPER_PC));

            assertTrue(pap.graph().getChildren(baseOA).contains(SUPER_OA));
            assertTrue(pap.graph().getParents(baseOA).contains(SUPER_PC));

            assertTrue(pap.graph().getChildren(SUPER_UA).contains(SUPER_USER));
            assertTrue(pap.graph().getParents(SUPER_UA).contains(SUPER_PC));

            assertTrue(pap.graph().getChildren(SUPER_OA).contains(SUPER_OBJECT));
            assertTrue(pap.graph().getParents(SUPER_OA).contains(baseOA));

            assertTrue(pap.graph().getAssociationsWithSource(SUPER_UA).containsAll(List.of(
                    new Association(SUPER_UA, baseUA, ALL_ACCESS_RIGHTS_SET),
                    new Association(SUPER_UA, SUPER_OA, ALL_ACCESS_RIGHTS_SET)
            )));

            assertTrue(pap.graph().getAssociationsWithTarget(baseUA).contains(
                    new Association(SUPER_UA, baseUA, ALL_ACCESS_RIGHTS_SET)
            ));

            assertTrue(pap.graph().getAssociationsWithTarget(SUPER_OA).contains(
                    new Association(SUPER_UA, SUPER_OA, ALL_ACCESS_RIGHTS_SET)
            ));
        });
    }

    @Test
    public void testSetResourceAccessRights() throws PMException {
        runTest(pap -> {
            AccessRightSet arset = new AccessRightSet("read", "write");
            pap.graph().setResourceAccessRights(arset);
            assertEquals(arset, pap.graph().getResourceAccessRights());
        });
    }

    @Nested
    class CreatePolicyClassTest {
        @Test
        public void NameAlreadyExists() throws PMException {
            runTest(pap -> assertThrows(NodeNameExistsException.class, () -> pap.graph().createPolicyClass(SUPER_PC, noprops())));
        }

        @Test
        public void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());

                String baseUA = Naming.baseUserAttribute("pc1");
                String baseOA = Naming.baseObjectAttribute("pc1");
                String rep = Naming.pcRepObjectAttribute("pc1");

                assertTrue(pap.graph().nodeExists(rep));
                assertTrue(pap.graph().nodeExists(baseOA));
                assertTrue(pap.graph().nodeExists(baseUA));

                assertTrue(pap.graph().getChildren("pc1").containsAll(List.of(baseUA, baseOA)));
                assertTrue(pap.graph().getParents(baseUA).contains("pc1"));
                assertTrue(pap.graph().getParents(baseOA).contains("pc1"));
                assertTrue(pap.graph().getChildren(baseUA).contains(SUPER_USER));
                assertTrue(pap.graph().getChildren("pc1").contains(rep));

                assertTrue(pap.graph().getAssociationsWithSource(SUPER_UA).containsAll(List.of(
                        new Association(SUPER_UA, baseUA, ALL_ACCESS_RIGHTS_SET),
                        new Association(SUPER_UA, baseOA, ALL_ACCESS_RIGHTS_SET)
                )));

                assertTrue(pap.graph().getAssociationsWithTarget(baseUA).contains(
                        new Association(SUPER_UA, baseUA, ALL_ACCESS_RIGHTS_SET)
                ));

                assertTrue(pap.graph().getAssociationsWithTarget(baseOA).contains(
                        new Association(SUPER_UA, baseOA, ALL_ACCESS_RIGHTS_SET)
                ));
            });
        }
    }

    @Nested
    class CreateObjectAttribute {

        @Test
        void NameAlreadyExists() throws PMException {
            runTest(pap -> assertThrows(NodeNameExistsException.class, () -> pap.graph().createObjectAttribute(SUPER_OA, noprops(), "pc1")));
        }

        @Test
        void ParentDoesNotExist() throws PMException {
            runTest(pap -> {
                assertThrows(NodeDoesNotExistException.class, () -> pap.graph().createObjectAttribute("oa1", noprops(), "pc1"));
                assertThrows(NodeDoesNotExistException.class, () -> pap.graph().createObjectAttribute("oa1", noprops(), SUPER_OA, "pc1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createObjectAttribute("oa1", noprops(), SUPER_OA);
                pap.graph().createObjectAttribute("oa2", toProperties("k", "v"), SUPER_OA, "oa1");

                assertTrue(pap.graph().nodeExists("oa1"));
                assertTrue(pap.graph().nodeExists("oa2"));
                assertEquals("v", pap.graph().getNode("oa2").getProperties().get("k"));

                assertTrue(pap.graph().getChildren(SUPER_OA).containsAll(List.of("oa1", "oa2")));
                assertTrue(pap.graph().getParents("oa1").contains(SUPER_OA));
                assertTrue(pap.graph().getChildren("oa1").contains("oa2"));
                assertTrue(pap.graph().getParents("oa2").containsAll(List.of(SUPER_OA, "oa1")));
            });
        }
    }

    @Nested
    class CreateUserAttributeTest {

        @Test
        void NameAlreadyExists() throws PMException {
            runTest(pap -> assertThrows(NodeNameExistsException.class, () -> pap.graph().createUserAttribute(SUPER_UA, noprops(), "pc1")));
        }

        @Test
        void ParentDoesNotExist() throws PMException {
            runTest(pap -> {
                assertThrows(NodeDoesNotExistException.class, () -> pap.graph().createUserAttribute("ua1", noprops(), "pc1"));
                assertThrows(NodeDoesNotExistException.class, () -> pap.graph().createUserAttribute("ua1", noprops(), SUPER_UA, "pc1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createUserAttribute("ua1", noprops(), SUPER_UA);
                pap.graph().createUserAttribute("ua2", toProperties("k", "v"), SUPER_UA, "ua1");

                assertTrue(pap.graph().nodeExists("ua1"));
                assertTrue(pap.graph().nodeExists("ua2"));
                assertEquals("v", pap.graph().getNode("ua2").getProperties().get("k"));

                assertTrue(pap.graph().getChildren(SUPER_UA).containsAll(List.of("ua1", "ua2")));
                assertTrue(pap.graph().getParents("ua1").contains(SUPER_UA));
                assertTrue(pap.graph().getChildren("ua1").contains("ua2"));
                assertTrue(pap.graph().getParents("ua2").containsAll(List.of(SUPER_UA, "ua1")));
            });
        }
    }

    @Nested
    class CreateObjectTest {

        @Test
        void NameAlreadyExists() throws PMException {
            runTest(pap -> assertThrows(NodeNameExistsException.class, () -> pap.graph().createObject(SUPER_OA, noprops(), "oa1")));
        }

        @Test
        void ParentDoesNotExist() throws PMException {
            runTest(pap -> {
                assertThrows(NodeDoesNotExistException.class, () -> pap.graph().createObject("o1", noprops(), "oa1"));
                assertThrows(NodeDoesNotExistException.class, () -> pap.graph().createObject("o1", noprops(), SUPER_OA, "oa1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createObject("o1", toProperties("k", "v"), SUPER_OA, Naming.baseObjectAttribute(SUPER_PC));

                assertTrue(pap.graph().nodeExists("o1"));
                assertEquals("v", pap.graph().getNode("o1").getProperties().get("k"));

                assertTrue(pap.graph().getChildren(SUPER_OA).contains("o1"));
                assertTrue(pap.graph().getParents("o1").containsAll(List.of(SUPER_OA, Naming.baseObjectAttribute(SUPER_PC))));
                assertTrue(pap.graph().getChildren(SUPER_OA).contains("o1"));
                assertTrue(pap.graph().getChildren(Naming.baseObjectAttribute(SUPER_PC)).contains("o1"));
            });
        }
    }

    @Nested
    class CreateUserTest {

        @Test
        void NameAlreadyExists() throws PMException {
            runTest(pap -> assertThrows(NodeNameExistsException.class, () -> pap.graph().createUser(SUPER_USER, noprops(), "ua1")));
        }

        @Test
        void ParentDoesNotExist() throws PMException {
            runTest(pap -> {
                assertThrows(NodeDoesNotExistException.class, () -> pap.graph().createUser("u1", noprops(), "ua1"));
                assertThrows(NodeDoesNotExistException.class, () -> pap.graph().createUser("u1", noprops(), SUPER_UA, "ua1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createUser("u1", toProperties("k", "v"), SUPER_UA, Naming.baseUserAttribute(SUPER_PC));

                assertTrue(pap.graph().nodeExists("u1"));
                assertEquals("v", pap.graph().getNode("u1").getProperties().get("k"));

                assertTrue(pap.graph().getChildren(SUPER_UA).contains("u1"));
                assertTrue(pap.graph().getParents("u1").containsAll(List.of(SUPER_UA, Naming.baseUserAttribute(SUPER_PC))));
                assertTrue(pap.graph().getChildren(SUPER_UA).contains("u1"));
                assertTrue(pap.graph().getChildren(Naming.baseUserAttribute(SUPER_PC)).contains("u1"));
            });
        }
    }

    @Nested
    class SetNodePropertiesTest {

        @Test
        void NodeDoesNotExist() throws PMException {
            runTest(pap -> assertThrows(NodeDoesNotExistException.class,
                    () -> pap.graph().setNodeProperties("oa1", noprops())));
        }

        @Test
        void NullProperties() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().setNodeProperties("pc1", noprops());

                assertTrue(pap.graph().getNode("pc1").getProperties().isEmpty());
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().setNodeProperties("pc1", toProperties("k", "v"));

                assertEquals("v", pap.graph().getNode("pc1").getProperties().get("k"));
            });
        }
    }

    @Nested
    class DeleteNodeTest {

        @Test
        void NullNameOrNodeDoesNotExist() throws PMException {
            runTest(pap -> {
                assertDoesNotThrow(() -> pap.graph().deleteNode(null));
                assertDoesNotThrow(() -> pap.graph().deleteNode("pc1"));
            });
        }

        @Test
        void DeletePolicyClassHasChildren() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");

                assertThrows(NodeHasChildrenException.class,
                        () -> pap.graph().deleteNode("pc1"));
            });
        }

        @Test
        void DeletePolicyClass() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().deleteNode("pc1");
                assertFalse(pap.graph().nodeExists("pc1"));
                assertFalse(pap.graph().nodeExists(Naming.baseUserAttribute("pc1")));
                assertFalse(pap.graph().nodeExists(Naming.baseObjectAttribute("pc1")));
                assertFalse(pap.graph().nodeExists(Naming.pcRepObjectAttribute("pc1")));
            });
        }

        @Test
        void DeleteNodeHasChildren() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa2", noprops(), "oa1");

                assertThrows(NodeHasChildrenException.class,
                        () -> pap.graph().deleteNode("oa1"));
            });
        }

        @Test
        void DeleteNodeWithProhibitionsAndObligations() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("ua1", noprops(), "pc1");
                pap.graph().createUserAttribute("oa1", noprops(), "pc1");
                pap.prohibitions().create("label", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet(), true, new ContainerCondition("oa1", true));
                pap.obligations().create(new UserContext(SUPER_USER), "oblLabel",
                        new Rule(
                                "rule1",
                                new EventPattern(
                                        EventSubject.anyUserWithAttribute("ua1"),
                                        new Performs("event1")
                                ),
                                new Response(new UserContext(""))
                        ),
                        new Rule(
                                "rule1",
                                new EventPattern(
                                        EventSubject.users("ua1"),
                                        new Performs("event1")
                                ),
                                new Response(new UserContext(""))
                        )
                );

                assertThrows(NodeReferencedInProhibitionException.class,
                        () -> pap.graph().deleteNode("ua1"));
                assertThrows(NodeReferencedInProhibitionException.class,
                        () -> pap.graph().deleteNode("oa1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");

                pap.graph().deleteNode("oa1");

                assertFalse(pap.graph().nodeExists("oa1"));
            });
        }
    }

    @Nested
    class GetNodeTest {

        @Test
        void DoesNotExist() throws PMException {
            runTest(pap -> assertThrows(NodeDoesNotExistException.class, () -> pap.graph().getNode("pc1")));
        }

        @Test
        void GetPolicyClass() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", Properties.toProperties("k", "v"));

                Node pc1 = pap.graph().getNode("pc1");

                assertEquals("pc1", pc1.getName());
                assertEquals(PC, pc1.getType());
                assertEquals("v", pc1.getProperties().get("k"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", Properties.toProperties("k", "v"), "pc1");

                Node oa1 = pap.graph().getNode("oa1");

                assertEquals("oa1", oa1.getName());
                assertEquals(OA, oa1.getType());
                assertEquals("v", oa1.getProperties().get("k"));
            });
        }
    }

    @Test
    void testSearch() throws PMException {
        runTest(pap -> {
            pap.graph().createPolicyClass("pc1", noprops());
            pap.graph().createObjectAttribute("oa1", toProperties("namespace", "test"), "pc1");
            pap.graph().createObjectAttribute("oa2", toProperties("key1", "value1"), "pc1");
            pap.graph().createObjectAttribute("oa3", toProperties("key1", "value1", "key2", "value2"), "pc1");

            List<String> nodes = pap.graph().search(OA, noprops());
            assertEquals(7, nodes.size());

            nodes = pap.graph().search(null, toProperties("key1", "value1"));
            assertEquals(2, nodes.size());

            nodes = pap.graph().search(null, toProperties("namespace", "test"));
            assertEquals(1, nodes.size());

            nodes = pap.graph().search(OA, toProperties("namespace", "test"));
            assertEquals(1, nodes.size());
            nodes = pap.graph().search(OA, toProperties("key1", "value1"));
            assertEquals(2, nodes.size());
            nodes = pap.graph().search(OA, toProperties("key1", "*"));
            assertEquals(2, nodes.size());
            nodes = pap.graph().search(OA, toProperties("key1", "value1", "key2", "value2"));
            assertEquals(1, nodes.size());
            nodes = pap.graph().search(OA, toProperties("key1", "value1", "key2", "*"));
            assertEquals(1, nodes.size());
            nodes = pap.graph().search(OA, toProperties("key1", "value1", "key2", "no_value"));
            assertEquals(0, nodes.size());
            nodes = pap.graph().search(null, noprops());
            assertEquals(14, nodes.size());
        });
    }

    @Test
    void testGetPolicyClasses() throws PMException {
        runTest(pap -> {
            pap.graph().createPolicyClass("pc1", noprops());
            pap.graph().createPolicyClass("pc2", noprops());
            pap.graph().createPolicyClass("pc3", noprops());

            assertTrue(pap.graph().getPolicyClasses().containsAll(Arrays.asList(SUPER_PC, "pc1", "pc2", "pc3")));
        });
    }

    @Test
    void testNodeExists() throws PMException {
        runTest(pap -> {
            assertTrue(pap.graph().nodeExists(SUPER_PC));
            assertTrue(pap.graph().nodeExists(SUPER_OA));
            assertTrue(pap.graph().nodeExists(SUPER_UA));
            assertTrue(pap.graph().nodeExists(SUPER_OBJECT));
            assertTrue(pap.graph().nodeExists(SUPER_USER));
            assertFalse(pap.graph().nodeExists("pc1"));
        });
    }

    @Nested
    class AssignTest {

        @Test
        void ChildNodeDoesNotExist() throws PMException {
            runTest(pap -> assertThrows(NodeDoesNotExistException.class,
                    () -> pap.graph().assign("oa1", "pc1")));
        }

        @Test
        void ParentNodeDoesNotExist() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.graph().assign("oa1", "oa2"));
            });
        }

        @Test
        void AssignmentExistsDoesNothing() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().assign("oa1", "pc1");
            });
        }

        @Test
        void InvalidAssignment() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createUserAttribute("ua1", noprops(), "pc1");

                assertThrows(InvalidAssignmentException.class,
                        () -> pap.graph().assign("ua1", "oa1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa2", noprops(), "pc1");
                pap.graph().assign("oa2", "oa1");
                assertTrue(pap.graph().getParents("oa2").contains("oa1"));
                assertTrue(pap.graph().getChildren("oa1").contains("oa2"));
            });
        }
    }

    @Nested
    class GetChildrenTest {

        @Test
        void NodeDoesNotExist() throws PMException {
            runTest(pap -> assertThrows(NodeDoesNotExistException.class,
                    () -> pap.graph().getChildren("oa1")));
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa2", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa3", noprops(), "pc1");


                assertTrue(pap.graph().getChildren("pc1").containsAll(List.of("oa1", "oa2", "oa3")));
            });
        }
    }

    @Nested
    class GetParentsTest {

        @Test
        void NodeDoesNotExist() throws PMException {
            runTest(pap -> assertThrows(NodeDoesNotExistException.class,
                    () -> pap.graph().getParents("oa1")));
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa2", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa3", noprops(), "pc1");
                pap.graph().createObject("o1", noprops(), "oa1");
                pap.graph().assign("o1", "oa2");
                pap.graph().assign("o1", "oa3");

                assertTrue(pap.graph().getParents("o1").containsAll(List.of("oa1", "oa2", "oa3")));
            });
        }
    }

    @Nested
    class DeassignTest {

        @Test
        void ChildNodeDoesNotExistDoesNothing() throws PMException {
            runTest(pap -> pap.graph().deassign("oa1", "pc1"));
        }

        @Test
        void ParentNodeDoesNotExistDoesNothing() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().deassign("oa1", "oa2");
            });
        }

        @Test
        void AssignmentDoesNotExistDoesNothing() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa2", noprops(), "pc1");
                pap.graph().deassign("oa1", "oa2");
            });
        }

        @Test
        void DisconnectedNode() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");

                assertThrows(DisconnectedNodeException.class,
                        () -> pap.graph().deassign("oa1", "pc1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createPolicyClass("pc2", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1", "pc2");
                pap.graph().deassign("oa1", "pc1");
                assertEquals(List.of("pc2"), pap.graph().getParents("oa1"));
                assertFalse(pap.graph().getParents("oa1").contains("pc1"));
                assertFalse(pap.graph().getChildren("pc1").contains("oa1"));
            });
        }

    }

    @Nested
    class AssociateTest {

        @Test
        void NodeDoesNotExist() throws PMException {
            runTest(pap -> {
                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.graph().associate("ua1", "oa1", new AccessRightSet()));

                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("ua1", noprops(), "pc1");

                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.graph().associate("ua1", "oa1", new AccessRightSet()));
            });
        }

        @Test
        void NodesAlreadyAssigned() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("ua1", noprops(), "pc1");
                pap.graph().createUserAttribute("ua2", noprops(), "ua1");
                assertThrows(NodesAlreadyAssignedException.class,
                        () -> pap.graph().associate("ua2", "ua1", new AccessRightSet()));
            });
        }

        @Test
        void UnknownResourceAccessRight() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("ua1", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                assertThrows(UnknownResourceAccessRightException.class,
                        () -> pap.graph().associate("ua1", "oa1", new AccessRightSet("read")));
                pap.graph().setResourceAccessRights(new AccessRightSet("read"));
                assertThrows(UnknownResourceAccessRightException.class,
                        () -> pap.graph().associate("ua1", "oa1", new AccessRightSet("write")));
                assertDoesNotThrow(() -> pap.graph().associate("ua1", "oa1", new AccessRightSet("read")));
                assertDoesNotThrow(() -> pap.graph().associate("ua1", "oa1", new AccessRightSet(ALL_ACCESS_RIGHTS)));
                assertDoesNotThrow(() -> pap.graph().associate("ua1", "oa1", new AccessRightSet(ALL_RESOURCE_ACCESS_RIGHTS)));
                assertDoesNotThrow(() -> pap.graph().associate("ua1", "oa1", new AccessRightSet(ALL_ADMIN_ACCESS_RIGHTS)));
            });
        }

        @Test
        void InvalidAssociation() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("ua1", noprops(), "pc1");
                pap.graph().createUserAttribute("ua2", noprops(), "ua1");
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa2", noprops(), "pc1");

                assertThrows(InvalidAssociationException.class,
                        () -> pap.graph().associate("ua2", "pc1", new AccessRightSet()));
                assertThrows(InvalidAssociationException.class,
                        () -> pap.graph().associate("oa1", "oa2", new AccessRightSet()));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("ua1", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");

                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
                pap.graph().associate("ua1", "oa1", new AccessRightSet("read"));

                assertTrue(
                        pap.graph().getAssociationsWithSource("ua1")
                                .contains(new Association("ua1", "oa1"))
                );
                assertTrue(
                        pap.graph().getAssociationsWithTarget("oa1")
                                .contains(new Association("ua1", "oa1"))
                );
            });
        }

        @Test
        void OverwriteSuccess() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("ua1", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");

                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
                pap.graph().associate("ua1", "oa1", new AccessRightSet("read"));

                List<Association> assocs = pap.graph().getAssociationsWithSource("ua1");
                Association assoc = assocs.get(0);
                assertEquals("ua1", assoc.getSource());
                assertEquals("oa1", assoc.getTarget());
                assertEquals(new AccessRightSet("read"), assoc.getAccessRightSet());

                pap.graph().associate("ua1", "oa1", new AccessRightSet("read", "write"));

                assocs = pap.graph().getAssociationsWithSource("ua1");
                assoc = assocs.get(0);
                assertEquals("ua1", assoc.getSource());
                assertEquals("oa1", assoc.getTarget());
                assertEquals(new AccessRightSet("read", "write"), assoc.getAccessRightSet());
            });
        }
    }

    @Nested
    class DissociateTest {

        @Test
        void NodeDoesNotExistDoesNothing() throws PMException {
            runTest(pap -> {
                assertDoesNotThrow(() -> pap.graph().dissociate("ua1", "oa1"));

                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createUserAttribute("ua1", noprops(), "pc1");

                assertDoesNotThrow(() -> pap.graph().dissociate("ua1", "oa2"));
            });
        }

        @Test
        void AssociationDoesNotExistDoesNothing() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createUserAttribute("ua1", noprops(), "pc1");

                assertDoesNotThrow(() -> pap.graph().dissociate("ua1", "oa1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createUserAttribute("ua1", noprops(), "pc1");
                pap.graph().associate("ua1", "oa1", new AccessRightSet());

                pap.graph().dissociate("ua1", "oa1");

                assertFalse(pap.graph().getAssociationsWithSource("ua1")
                        .contains(new Association("ua1", "oa1")));
                assertFalse(pap.graph().getAssociationsWithTarget("oa1")
                        .contains(new Association("ua1", "oa1")));
            });
        }
    }

    @Nested
    class GetAssociationsWithSourceTest {

        @Test
        void NodeDoesNotExist() throws PMException {
            runTest(pap -> assertThrows(NodeDoesNotExistException.class,
                    () -> pap.graph().getAssociationsWithSource("ua1")));
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa2", noprops(), "pc1");
                pap.graph().createUserAttribute("ua1", noprops(), "pc1");
                pap.graph().associate("ua1", "oa1", new AccessRightSet("read"));
                pap.graph().associate("ua1", "oa2", new AccessRightSet("read", "write"));

                List<Association> assocs = pap.graph().getAssociationsWithSource("ua1");

                assertEquals(2, assocs.size());

                for (Association assoc : assocs) {
                    checkAssociation(assoc);
                }
            });
        }

        private void checkAssociation(Association association) {
            if (association.getTarget().equals("oa1")) {
                assertEquals(new AccessRightSet("read"), association.getAccessRightSet());
            } else if (association.getTarget().equals("oa2")) {
                assertEquals(new AccessRightSet("read", "write"), association.getAccessRightSet());
            }
        }
    }

    @Nested
    class GetAssociationsWithTargetTest {

        @Test
        void NodeDoesNotExist() throws PMException {
            runTest(pap -> assertThrows(NodeDoesNotExistException.class,
                    () -> pap.graph().getAssociationsWithTarget("oa1")));
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createUserAttribute("ua1", noprops(), "pc1");
                pap.graph().createUserAttribute("ua2", noprops(), "pc1");
                pap.graph().associate("ua1", "oa1", new AccessRightSet("read"));
                pap.graph().associate("ua2", "oa1", new AccessRightSet("read", "write"));

                List<Association> assocs = pap.graph().getAssociationsWithTarget("oa1");

                assertEquals(2, assocs.size());

                for (Association assoc : assocs) {
                    checkAssociation(assoc);
                }
            });
        }

        private void checkAssociation(Association association) {
            if (association.getSource().equals("ua1")) {
                assertEquals(new AccessRightSet("read"), association.getAccessRightSet());
            } else if (association.getSource().equals("ua2")) {
                assertEquals(new AccessRightSet("read", "write"), association.getAccessRightSet());
            }
        }
    }

    @Nested
    class CreateProhibitionTest {

        @Test
        void ProhibitionExists() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("subject", noprops(), "pc1");

                pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet(), false);

                assertThrows(ProhibitionExistsException.class,
                        () -> pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet(), false));
            });
        }

        @Test
        void InvalidAccessRights() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("subject", noprops(), "pc1");

                assertThrows(UnknownResourceAccessRightException.class,
                        () -> pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), false));
            });
        }

        @Test
        void ContainerDoesNotExist() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("subject", noprops(), "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read"));
                assertThrows(ProhibitionContainerDoesNotExistException.class,
                        () -> pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), false, new ContainerCondition("oa1", true)));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("subject", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa2", noprops(), "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));

                Prohibition p = pap.prohibitions().get("label");
                assertEquals("label", p.getLabel());
                assertEquals("subject", p.getSubject().name());
                assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                assertTrue(p.isIntersection());
                assertEquals(2, p.getContainers().size());
                assertEquals(List.of(
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false)
                ), p.getContainers());
            });
        }
    }

    @Nested
    class UpdateProhibitionTest {

        @Test
        void ProhibitionDoesNotExist() throws PMException {
            runTest(pap -> assertThrows(ProhibitionDoesNotExistException.class,
                    () -> pap.prohibitions().update("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet(), false)));
        }

        @Test
        void InvalidAccessRights() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("subject", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true));

                assertThrows(UnknownResourceAccessRightException.class,
                        () -> pap.prohibitions().update("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("test"), false));
            });
        }

        @Test
        void SubjectDoesNotExist() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("subject", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true));

                assertThrows(ProhibitionSubjectDoesNotExistException.class,
                        () -> pap.prohibitions().update("label", ProhibitionSubject.userAttribute("test"), new AccessRightSet("read"), false));
                assertDoesNotThrow(() -> pap.prohibitions().update("label", ProhibitionSubject.process("subject"), new AccessRightSet("read"), false));
            });
        }

        @Test
        void ContainerDoesNotExist() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("subject", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true));

                assertThrows(ProhibitionContainerDoesNotExistException.class,
                        () -> pap.prohibitions().update("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), false, new ContainerCondition("oa3", true)));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("subject", noprops(), "pc1");
                pap.graph().createUserAttribute("subject2", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa2", noprops(), "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));
                pap.prohibitions().update("label", ProhibitionSubject.userAttribute("subject2"), new AccessRightSet("read", "write"), true,
                        new ContainerCondition("oa1", false),
                        new ContainerCondition("oa2", true));

                Prohibition p = pap.prohibitions().get("label");
                assertEquals("label", p.getLabel());
                assertEquals("subject2", p.getSubject().name());
                assertEquals(new AccessRightSet("read", "write"), p.getAccessRightSet());
                assertTrue(p.isIntersection());
                assertEquals(2, p.getContainers().size());
                assertEquals(List.of(
                        new ContainerCondition("oa1", false),
                        new ContainerCondition("oa2", true)
                ), p.getContainers());
            });
        }

        @Nested
        class DeleteProhibitionTest {

            @Test
            void ProhibitionDoesNotExist() throws PMException {
                runTest(pap -> assertThrows(ProhibitionDoesNotExistException.class,
                        () -> pap.prohibitions().update("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet(), false)));
            }

            @Test
            void Success() throws PMException {
                runTest(pap -> {
                    pap.graph().createPolicyClass("pc1", noprops());
                    pap.graph().createUserAttribute("subject", noprops(), "pc1");
                    pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                    pap.graph().createObjectAttribute("oa2", noprops(), "pc1");
                    pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                    pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                            new ContainerCondition("oa1", true),
                            new ContainerCondition("oa2", false));

                    pap.prohibitions().delete("label");

                    assertThrows(ProhibitionDoesNotExistException.class,
                            () -> pap.prohibitions().get("label"));
                });
            }
        }
    }

    @Nested
    class GetProhibitionsTest {

        @Test
        void GetProhibitions() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("subject", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa2", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa3", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa4", noprops(), "pc1");

                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("label1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));
                pap.prohibitions().create("label2", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa3", true),
                        new ContainerCondition("oa4", false));

                Map<String, List<Prohibition>> prohibitions = pap.prohibitions().getAll();
                assertEquals(1, prohibitions.size());
                assertEquals(2, prohibitions.get("subject").size());
                checkProhibitions(prohibitions.get("subject"));
            });
        }

        private void checkProhibitions(List<Prohibition> prohibitions) {
            for (Prohibition p : prohibitions) {
                if (p.getLabel().equals("label1")) {
                    assertEquals("label1", p.getLabel());
                    assertEquals("subject", p.getSubject().name());
                    assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                    assertTrue(p.isIntersection());
                    assertEquals(2, p.getContainers().size());
                    assertEquals(List.of(
                            new ContainerCondition("oa1", true),
                            new ContainerCondition("oa2", false)
                    ), p.getContainers());
                } else if (p.getLabel().equals("label2")) {
                    assertEquals("label2", p.getLabel());
                    assertEquals("subject", p.getSubject().name());
                    assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                    assertTrue(p.isIntersection());
                    assertEquals(2, p.getContainers().size());
                    assertEquals(List.of(
                            new ContainerCondition("oa3", true),
                            new ContainerCondition("oa4", false)
                    ), p.getContainers());
                } else {
                    fail("unexpected prohibition label " + p.getLabel());
                }
            }
        }

        @Test
        void GetProhibitionsFor() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("subject", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa2", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa3", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa4", noprops(), "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("label1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));
                pap.prohibitions().create("label2", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa3", true),
                        new ContainerCondition("oa4", false));

                List<Prohibition> prohibitions = pap.prohibitions().getWithSubject("subject");
                assertEquals(2, prohibitions.size());
                checkProhibitions(prohibitions);
            });
        }

        @Test
        void GetProhibition() throws PMException {
            runTest(pap -> {
                assertThrows(ProhibitionDoesNotExistException.class,
                        () -> pap.prohibitions().get("label"));

                pap.graph().createPolicyClass("pc1", noprops());
                pap.graph().createUserAttribute("subject", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa2", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa3", noprops(), "pc1");
                pap.graph().createObjectAttribute("oa4", noprops(), "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("label1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));
                pap.prohibitions().create("label2", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa3", true),
                        new ContainerCondition("oa4", false));

                Prohibition p = pap.prohibitions().get("label1");
                assertEquals("label1", p.getLabel());
                assertEquals("subject", p.getSubject().name());
                assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                assertTrue(p.isIntersection());
                assertEquals(2, p.getContainers().size());
                assertEquals(List.of(
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false)
                ), p.getContainers());
            });
        }
    }

    @Nested
    class ObligationTest {

        Obligation obligation1 = new Obligation(
                new UserContext(SUPER_USER),
                "label",
                List.of(
                        new Rule(
                                "rule1",
                                new EventPattern(
                                        EventSubject.anyUser(),
                                        new Performs("test_event")
                                ),
                                new Response(
                                        new UserContext(SUPER_USER),
                                        new CreatePolicyStatement(new Expression(new Literal("test_pc")))
                                )
                        )
                )
        );

        Obligation obligation2 = new Obligation(
                new UserContext(SUPER_USER),
                "label2")
                .addRule(
                        new Rule(
                                "rule1",
                                new EventPattern(
                                        EventSubject.anyUser(),
                                        new Performs("test_event")
                                ),
                                new Response(
                                        new UserContext(SUPER_USER),
                                        new CreatePolicyStatement(new Expression(new Literal("test_pc")))
                                )
                        )
                ).addRule(
                        new Rule(
                                "rule2",
                                new EventPattern(
                                        EventSubject.anyUser(),
                                        new Performs("test_event")
                                ),
                                new Response(
                                        new UserContext(SUPER_USER),
                                        new CreatePolicyStatement(new Expression(new Literal("test_pc")))
                                )
                        )
                );


        @Nested
        class CreateObligation {

            @Test
            void AuthorDoesNotExist() throws PMException {
                runTest(pap -> assertThrows(NodeDoesNotExistException.class,
                        () -> pap.obligations().create(new UserContext("u1"), obligation1.getLabel(),
                                obligation1.getRules().toArray(Rule[]::new))));
            }

            @Test
            void EventSubjectDoesNotExist() throws PMException {
                runTest(pap -> {
                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.obligations().create(
                                    new UserContext(SUPER_USER),
                                    "label",
                                    new Rule(
                                            "rule1",
                                            new EventPattern(
                                                    EventSubject.users("u1"),
                                                    Performs.events("test_event"),
                                                    Target.anyPolicyElement()
                                            ),
                                            new Response(new UserContext(SUPER_USER))
                                    )
                            ));
                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.obligations().create(
                                    new UserContext(SUPER_USER),
                                    "label",
                                    new Rule(
                                            "rule1",
                                            new EventPattern(
                                                    EventSubject.anyUserWithAttribute("ua1"),
                                                    Performs.events("test_event"),
                                                    Target.anyPolicyElement()
                                            ),
                                            new Response(new UserContext(SUPER_USER))
                                    )
                            ));
                });
            }

            @Test
            void EventTargetDoesNotExist() throws PMException {
                runTest(pap -> {
                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.obligations().create(
                                    new UserContext(SUPER_USER),
                                    "label",
                                    new Rule(
                                            "rule1",
                                            new EventPattern(
                                                    EventSubject.users(SUPER_USER),
                                                    Performs.events("test_event"),
                                                    Target.anyOfSet("oa1")
                                            ),
                                            new Response(new UserContext(SUPER_USER))
                                    )
                            ));
                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.obligations().create(
                                    new UserContext(SUPER_USER),
                                    "label",
                                    new Rule(
                                            "rule1",
                                            new EventPattern(
                                                    EventSubject.users(SUPER_USER),
                                                    Performs.events("test_event"),
                                                    Target.policyElement("oa1")
                                            ),
                                            new Response(new UserContext(SUPER_USER))
                                    )
                            ));
                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.obligations().create(
                                    new UserContext(SUPER_USER),
                                    "label",
                                    new Rule(
                                            "rule1",
                                            new EventPattern(
                                                    EventSubject.users(SUPER_USER),
                                                    Performs.events("test_event"),
                                                    Target.anyContainedIn("oa1")
                                            ),
                                            new Response(new UserContext(SUPER_USER))
                                    )
                            ));
                });
            }

            @Test
            void Success() throws PMException {
                runTest(pap -> {
                    pap.obligations().create(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));

                    assertThrows(ObligationExistsException.class,
                            () -> pap.obligations().create(obligation1.getAuthor(), obligation1.getLabel()));

                    Obligation actual = pap.obligations().get(obligation1.getLabel());
                    assertEquals(obligation1, actual);
                });
            }
        }

        @Nested
        class UpdateObligation {

            @Test
            void AuthorDoesNotExist() throws PMException {
                runTest(pap -> {
                    pap.obligations().create(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));

                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.obligations().update(new UserContext("u1"), obligation1.getLabel(),
                                    obligation1.getRules().toArray(Rule[]::new)));
                });
            }

            @Test
            void EventSubjectDoesNotExist() throws PMException {
                runTest(pap -> {
                    pap.obligations().create(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));

                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.obligations().update(
                                    new UserContext(SUPER_USER),
                                    obligation1.getLabel(),
                                    new Rule(
                                            "rule1",
                                            new EventPattern(
                                                    EventSubject.users("u1"),
                                                    Performs.events("test_event"),
                                                    Target.anyPolicyElement()
                                            ),
                                            new Response(new UserContext(SUPER_USER))
                                    )
                            ));
                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.obligations().update(
                                    new UserContext(SUPER_USER),
                                    obligation1.getLabel(),
                                    new Rule(
                                            "rule1",
                                            new EventPattern(
                                                    EventSubject.anyUserWithAttribute("ua1"),
                                                    Performs.events("test_event"),
                                                    Target.anyPolicyElement()
                                            ),
                                            new Response(new UserContext(SUPER_USER))
                                    )
                            ));
                });
            }

            @Test
            void EventTargetDoesNotExist() throws PMException {
                runTest(pap -> {
                    pap.obligations().create(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));

                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.obligations().update(
                                    new UserContext(SUPER_USER),
                                    obligation1.getLabel(),
                                    new Rule(
                                            "rule1",
                                            new EventPattern(
                                                    EventSubject.users(SUPER_USER),
                                                    Performs.events("test_event"),
                                                    Target.anyOfSet("oa1")
                                            ),
                                            new Response(new UserContext(SUPER_USER))
                                    )
                            ));
                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.obligations().update(
                                    new UserContext(SUPER_USER),
                                    obligation1.getLabel(),
                                    new Rule(
                                            "rule1",
                                            new EventPattern(
                                                    EventSubject.users(SUPER_USER),
                                                    Performs.events("test_event"),
                                                    Target.policyElement("oa1")
                                            ),
                                            new Response(new UserContext(SUPER_USER))
                                    )
                            ));
                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.obligations().update(
                                    new UserContext(SUPER_USER),
                                    obligation1.getLabel(),
                                    new Rule(
                                            "rule1",
                                            new EventPattern(
                                                    EventSubject.users(SUPER_USER),
                                                    Performs.events("test_event"),
                                                    Target.anyContainedIn("oa1")
                                            ),
                                            new Response(new UserContext(SUPER_USER))
                                    )
                            ));
                });
            }

            @Test
            void Success() throws PMException {
                runTest(pap -> {
                    assertThrows(ObligationDoesNotExistException.class,
                            () -> pap.obligations().update(new UserContext(SUPER_USER), obligation1.getLabel()));

                    pap.obligations().create(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));

                    pap.obligations().update(new UserContext(SUPER_USER), obligation1.getLabel(),
                            obligation2.getRules().toArray(Rule[]::new));

                    Obligation expected = new Obligation(obligation1);
                    expected.setRules(obligation2.getRules());

                    Obligation actual = pap.obligations().get(obligation1.getLabel());
                    assertEquals(expected, actual);
                });
            }

        }

        @Test
        void DeleteObligation() throws PMException {
            runTest(pap -> {
                assertDoesNotThrow(() -> pap.obligations().delete(obligation1.getLabel()));

                pap.obligations().create(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));
                pap.obligations().create(obligation2.getAuthor(), obligation2.getLabel(), obligation2.getRules().toArray(Rule[]::new));

                pap.obligations().delete(obligation1.getLabel());

                assertThrows(ObligationDoesNotExistException.class,
                        () -> pap.obligations().get(obligation1.getLabel()));
            });
        }

        @Test
        void GetObligations() throws PMException {
            runTest(pap -> {
                pap.obligations().create(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));
                pap.obligations().create(obligation2.getAuthor(), obligation2.getLabel(), obligation2.getRules().toArray(Rule[]::new));

                List<Obligation> obligations = pap.obligations().getAll();
                assertEquals(2, obligations.size());
                for (Obligation obligation : obligations) {
                    if (obligation.getLabel().equals(obligation1.getLabel())) {
                        assertEquals(obligation1, obligation);
                    } else {
                        assertEquals(obligation2, obligation);
                    }
                }
            });
        }

        @Test
        void GetObligation() throws PMException {
            runTest(pap -> {
                assertThrows(ObligationDoesNotExistException.class,
                        () -> pap.obligations().get(obligation1.getLabel()));

                pap.obligations().create(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));
                pap.obligations().create(obligation2.getAuthor(), obligation2.getLabel(), obligation2.getRules().toArray(Rule[]::new));

                Obligation obligation = pap.obligations().get(obligation1.getLabel());
                assertEquals(obligation1, obligation);
            });
        }

    }

    @Test
    void testTx() throws PMException {
        runTest(pap -> {
            pap.beginTx();
            pap.graph().createPolicyClass("pc1", noprops());
            pap.graph().createObjectAttribute("oa1", noprops(), "pc1");
            pap.graph().createUserAttribute("ua1", noprops(), "pc1");
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

    @Nested
    class AddFunction {

        FunctionDefinitionStatement testFunc = new FunctionDefinitionStatement(
                "testFunc",
                Type.string(),
                Arrays.asList(
                        new FormalArgument("arg1", Type.string()),
                        new FormalArgument("arg2", Type.array(Type.string()))
                ),
                Arrays.asList(
                        new CreatePolicyStatement(new Expression(new Literal("pc1"))),
                        new CreateAttrStatement(
                                new Expression(new Literal("ua1")),
                                UA,
                                new Expression(new Literal("pc1"))
                        ),
                        new CreateAttrStatement(
                                new Expression(new Literal("oa1")),
                                OA,
                                new Expression(new Literal("pc1"))
                        )
                )
        );

        @Test
        void functionAlreadyDefined() throws PMException {
            runTest(pap -> {
                pap.pal().addFunction(testFunc);
                assertThrows(FunctionAlreadyDefinedException.class, () -> pap.pal().addFunction(testFunc));
            });
        }

        @Test
        void success() throws PMException {
            runTest(pap -> {
                pap.pal().addFunction(testFunc);
                assertTrue(pap.pal().getFunctions().containsKey(testFunc.getFunctionName()));
                FunctionDefinitionStatement actual = pap.pal().getFunctions().get(testFunc.getFunctionName());
                assertEquals(testFunc, actual);
            });
        }
    }

    @Nested
    class RemoveFunction {

        @Test
        void functionDoesNotExistNoException() throws PMException {
            runTest(pap -> {
                assertDoesNotThrow(() -> pap.pal().removeFunction("func"));
            });
        }

        @Test
        void success() throws PMException {
            runTest(pap -> {
                pap.pal().addFunction(new FunctionDefinitionStatement("testFunc", Type.voidType(), List.of(), List.of()));
                assertTrue(pap.pal().getFunctions().containsKey("testFunc"));
                pap.pal().removeFunction("testFunc");
                assertFalse(pap.pal().getFunctions().containsKey("testFunc"));
            });
        }
    }

    @Nested
    class GetFunctions {

        @Test
        void success() throws PMException {
            FunctionDefinitionStatement testFunc1 = new FunctionDefinitionStatement("testFunc1", Type.voidType(), List.of(), List.of());
            FunctionDefinitionStatement testFunc2 = new FunctionDefinitionStatement("testFunc2", Type.voidType(), List.of(), List.of());

            runTest(pap -> {

                pap.pal().addFunction(testFunc1);
                pap.pal().addFunction(testFunc2);

                Map<String, FunctionDefinitionStatement> functions = pap.pal().getFunctions();
                assertTrue(functions.containsKey("testFunc1"));
                FunctionDefinitionStatement actual = functions.get("testFunc1");
                assertEquals(testFunc1, actual);

                assertTrue(functions.containsKey("testFunc2"));
                actual = functions.get("testFunc2");
                assertEquals(testFunc2, actual);
            });
        }

    }

    @Nested
    class AddConstant {

        @Test
        void constantAlreadyDefined() throws PMException {
            runTest(pap -> {
                pap.pal().addConstant("const1", new Value("test"));
                assertThrows(ConstantAlreadyDefinedException.class, () -> pap.pal().addConstant("const1", new Value("test")));
            });
        }

        @Test
        void success() throws PMException {
            Value expected = new Value("test");

            runTest(pap -> {
                pap.pal().addConstant("const1", expected);
                assertTrue(pap.pal().getConstants().containsKey("const1"));
                Value actual = pap.pal().getConstants().get("const1");
                assertEquals(expected, actual);
            });
        }
    }

    @Nested
    class RemoveConstant {

        @Test
        void constantDoesNotExistNoException() throws PMException {
            runTest(pap -> {
                assertDoesNotThrow(() -> pap.pal().removeConstant("const1"));
            });
        }

        @Test
        void success() throws PMException {
            runTest(pap -> {
                pap.pal().addConstant("const1", new Value("test"));
                assertTrue(pap.pal().getConstants().containsKey("const1"));
                pap.pal().removeConstant("const1");
                assertFalse(pap.pal().getConstants().containsKey("const1"));
            });
        }
    }

    @Nested
    class GetConstants {

        @Test
        void success() throws PMException {
            Value const1 = new Value("test1");
            Value const2 = new Value("test2");

            runTest(pap -> {
                pap.pal().addConstant("const1", const1);
                pap.pal().addConstant("const2", const2);

                Map<String, Value> constants = pap.pal().getConstants();
                assertTrue(constants.containsKey("const1"));
                Value actual = constants.get("const1");
                assertEquals(const1, actual);

                assertTrue(constants.containsKey("const2"));
                actual = constants.get("const2");
                assertEquals(const2, actual);
            });
        }

    }
}