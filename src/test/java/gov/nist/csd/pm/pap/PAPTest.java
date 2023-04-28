package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.SamplePolicy;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.mysql.MysqlPolicyStore;
import gov.nist.csd.pm.pap.mysql.MysqlTestEnv;
import gov.nist.csd.pm.policy.PolicyEquals;
import gov.nist.csd.pm.policy.pml.model.expression.*;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
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
import gov.nist.csd.pm.policy.pml.statement.CreateAttrStatement;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.toProperties;
import static gov.nist.csd.pm.pap.SuperPolicy.*;
import static org.junit.jupiter.api.Assertions.*;

class PAPTest {

    private static MysqlTestEnv testEnv;

    public void runTest(TestRunner testRunner) throws PMException {
        testRunner.run(new PAP(new MemoryPolicyStore()));

        try (Connection connection = testEnv.getConnection()) {
            PAP mysqlPAP = new PAP(new MysqlPolicyStore(connection));
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
    void testSetResourceAccessRights() throws PMException {
        runTest(pap -> {
            AccessRightSet arset = new AccessRightSet("read", "write");
            pap.graph().setResourceAccessRights(arset);
            assertEquals(arset, pap.graph().getResourceAccessRights());

            assertThrows(AdminAccessRightExistsException.class, () -> pap.graph().setResourceAccessRights(new AccessRightSet(CREATE_POLICY_CLASS)));
        });
    }

    @Nested
    class CreatePolicyClassTest {
        @Test
        void NameAlreadyExists() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                assertDoesNotThrow(() -> pap.graph().createPolicyClass(SUPER_PC));
                assertThrows(NodeNameExistsException.class, () -> pap.graph().createPolicyClass("pc1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                String rep = SuperPolicy.pcRepObjectAttribute("pc1");
                assertTrue(pap.graph().nodeExists("pc1"));
                assertTrue(pap.graph().nodeExists(rep));
            });
        }
    }

    @Nested
    class CreateObjectAttribute {

        @Test
        void NameAlreadyExists() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                assertThrows(NodeNameExistsException.class,
                        () -> pap.graph().createObjectAttribute("oa1", "pc1"));
            });
        }

        @Test
        void ParentDoesNotExist() throws PMException {
            runTest(pap -> {
                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.graph().createObjectAttribute("oa1", "pc1"));

                pap.graph().createPolicyClass("pc1");

                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.graph().createObjectAttribute("oa1", "pc1", "pc2"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createObjectAttribute("oa1", SUPER_OA);
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
            runTest(pap -> {
                assertDoesNotThrow(() -> pap.graph().createUserAttribute("ua1", SUPER_UA));
                assertThrows(NodeNameExistsException.class, () -> pap.graph().createUserAttribute("ua1", "pc1"));
            });
        }

        @Test
        void ParentDoesNotExist() throws PMException {
            runTest(pap -> {
                assertThrows(NodeDoesNotExistException.class, () -> pap.graph().createUserAttribute("ua1", "pc1"));
                assertThrows(NodeDoesNotExistException.class, () -> pap.graph().createUserAttribute("ua1", SUPER_UA, "pc1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createUserAttribute("ua1", SUPER_UA);
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
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObject("o1", "oa1");

                assertThrows(NodeNameExistsException.class,
                        () -> pap.graph().createObject("o1", "oa1"));
            });
        }

        @Test
        void ParentDoesNotExist() throws PMException {
            runTest(pap -> {
                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.graph().createObject("o1", "oa1"));

                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.graph().createObject("o1", "oa1", "oa2"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createObject("o1", toProperties("k", "v"), SUPER_OA);

                assertTrue(pap.graph().nodeExists("o1"));
                assertEquals("v", pap.graph().getNode("o1").getProperties().get("k"));

                assertTrue(pap.graph().getChildren(SUPER_OA).contains("o1"));
                assertEquals( List.of(SUPER_OA), pap.graph().getParents("o1"));
                assertTrue(pap.graph().getChildren(SUPER_OA).contains("o1"));
            });
        }
    }

    @Nested
    class CreateUserTest {

        @Test
        void NameAlreadyExists() throws PMException {
            runTest(pap -> {
                pap.graph().createUserAttribute("ua1", SUPER_UA);
                pap.graph().createUser("u1", SUPER_UA);
                assertDoesNotThrow(() -> pap.graph().createUser(SUPER_USER, "ua1"));
                assertThrows(NodeNameExistsException.class, () -> pap.graph().createUser("u1", "ua1"));
            });
        }

        @Test
        void ParentDoesNotExist() throws PMException {
            runTest(pap -> {
                assertThrows(NodeDoesNotExistException.class, () -> pap.graph().createUser("u1", "ua1"));
                assertThrows(NodeDoesNotExistException.class, () -> pap.graph().createUser("u1", SUPER_UA, "ua1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createUser("u1", toProperties("k", "v"), SUPER_UA, SUPER_UA1);

                assertTrue(pap.graph().nodeExists("u1"));
                assertEquals("v", pap.graph().getNode("u1").getProperties().get("k"));

                assertTrue(pap.graph().getChildren(SUPER_UA).contains("u1"));
                assertTrue(pap.graph().getParents("u1").containsAll(List.of(SUPER_UA, SUPER_UA1)));
                assertTrue(pap.graph().getChildren(SUPER_UA).contains("u1"));
                assertTrue(pap.graph().getChildren(SUPER_UA1).contains("u1"));
            });
        }
    }

    @Nested
    class SetNodePropertiesTest {

        @Test
        void NodeDoesNotExist() throws PMException {
            runTest(pap -> assertThrows(NodeDoesNotExistException.class,
                    () -> pap.graph().setNodeProperties("oa1", NO_PROPERTIES)));
        }

        @Test
        void EmptyProperties() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().setNodeProperties("pc1", NO_PROPERTIES);

                assertTrue(pap.graph().getNode("pc1").getProperties().isEmpty());
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
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
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

                assertThrows(NodeHasChildrenException.class,
                        () -> pap.graph().deleteNode("pc1"));
            });
        }

        @Test
        void DeletePolicyClass() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().deleteNode("pc1");
                assertFalse(pap.graph().nodeExists("pc1"));
                assertFalse(pap.graph().nodeExists(SuperPolicy.pcRepObjectAttribute("pc1")));
            });
        }

        @Test
        void DeleteNodeHasChildren() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "oa1");

                assertThrows(NodeHasChildrenException.class,
                        () -> pap.graph().deleteNode("oa1"));
            });
        }

        @Test
        void DeleteNodeWithProhibitionsAndObligations() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUserAttribute("oa1", "pc1");
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
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

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
                pap.graph().createPolicyClass("pc1");
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
            pap.graph().createPolicyClass("pc1");
            pap.graph().createObjectAttribute("oa1", toProperties("namespace", "test"), "pc1");
            pap.graph().createObjectAttribute("oa2", toProperties("key1", "value1"), "pc1");
            pap.graph().createObjectAttribute("oa3", toProperties("key1", "value1", "key2", "value2"), "pc1");

            List<String> nodes = pap.graph().search(OA, NO_PROPERTIES);
            assertEquals(6, nodes.size());

            nodes = pap.graph().search(ANY, toProperties("key1", "value1"));
            assertEquals(2, nodes.size());

            nodes = pap.graph().search(ANY, toProperties("namespace", "test"));
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
            nodes = pap.graph().search(ANY, NO_PROPERTIES);
            assertEquals(11, nodes.size());
        });
    }

    @Test
    void testGetPolicyClasses() throws PMException {
        runTest(pap -> {
            pap.graph().createPolicyClass("pc1");
            pap.graph().createPolicyClass("pc2");
            pap.graph().createPolicyClass("pc3");

            assertTrue(pap.graph().getPolicyClasses().containsAll(Arrays.asList(SUPER_PC, "pc1", "pc2", "pc3")));
        });
    }

    @Test
    void testNodeExists() throws PMException {
        runTest(pap -> {
            assertTrue(pap.graph().nodeExists(SUPER_PC));
            assertTrue(pap.graph().nodeExists(SUPER_UA));
            assertTrue(pap.graph().nodeExists(SUPER_PC_REP));
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
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.graph().assign("oa1", "oa2"));
            });
        }

        @Test
        void AssignmentExistsDoesNothing() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().assign("oa1", "pc1");
            });
        }

        @Test
        void InvalidAssignment() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createUserAttribute("ua1", "pc1");

                assertThrows(InvalidAssignmentException.class,
                        () -> pap.graph().assign("ua1", "oa1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
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
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().createObjectAttribute("oa3", "pc1");


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
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().createObjectAttribute("oa3", "pc1");
                pap.graph().createObject("o1", "oa1");
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
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().deassign("oa1", "oa2");
            });
        }

        @Test
        void AssignmentDoesNotExistDoesNothing() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().deassign("oa1", "oa2");
            });
        }

        @Test
        void DisconnectedNode() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

                assertThrows(DisconnectedNodeException.class,
                        () -> pap.graph().deassign("oa1", "pc1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createPolicyClass("pc2");
                pap.graph().createObjectAttribute("oa1", "pc1", "pc2");
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

                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");

                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.graph().associate("ua1", "oa1", new AccessRightSet()));
            });
        }

        @Test
        void NodesAlreadyAssigned() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUserAttribute("ua2", "ua1");
                assertDoesNotThrow(() -> pap.graph().associate("ua2", "ua1", new AccessRightSet()));
            });
        }

        @Test
        void UnknownResourceAccessRight() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                assertThrows(UnknownAccessRightException.class,
                        () -> pap.graph().associate("ua1", "oa1", new AccessRightSet("read")));
                pap.graph().setResourceAccessRights(new AccessRightSet("read"));
                assertThrows(UnknownAccessRightException.class,
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
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUserAttribute("ua2", "ua1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");

                assertThrows(InvalidAssociationException.class,
                        () -> pap.graph().associate("ua2", "pc1", new AccessRightSet()));
                assertThrows(InvalidAssociationException.class,
                        () -> pap.graph().associate("oa1", "oa2", new AccessRightSet()));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

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
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");

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

                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createUserAttribute("ua1", "pc1");

                assertDoesNotThrow(() -> pap.graph().dissociate("ua1", "oa2"));
            });
        }

        @Test
        void AssociationDoesNotExistDoesNothing() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createUserAttribute("ua1", "pc1");

                assertDoesNotThrow(() -> pap.graph().dissociate("ua1", "oa1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
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
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
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
                pap.graph().createPolicyClass("pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createUserAttribute("ua1", "pc1");
                pap.graph().createUserAttribute("ua2", "pc1");
                pap.graph().associate("ua1", "oa1", new AccessRightSet("read"));
                pap.graph().associate("ua2", "oa1", new AccessRightSet("read", "write"));

                List<Association> assocs = pap.graph().getAssociationsWithTarget("oa1");

                assertEquals(3, assocs.size());

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
            } else if (association.getSource().equals(SUPER_UA)) {
                assertEquals(allAccessRights(), association.getAccessRightSet());
            }
        }
    }

    @Nested
    class CreateProhibitionTest {

        @Test
        void ProhibitionExists() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");

                pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet(), false);

                assertThrows(ProhibitionExistsException.class,
                        () -> pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet(), false));
            });
        }

        @Test
        void InvalidAccessRights() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");

                assertThrows(UnknownAccessRightException.class,
                        () -> pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), false));
            });
        }

        @Test
        void ContainerDoesNotExist() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read"));
                assertThrows(ProhibitionContainerDoesNotExistException.class,
                        () -> pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), false, new ContainerCondition("oa1", true)));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));

                Prohibition p = pap.prohibitions().get("label");
                assertEquals("label", p.getLabel());
                assertEquals("subject", p.getSubject().getName());
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
        void InvalidAccessRights() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true));

                assertThrows(UnknownAccessRightException.class,
                        () -> pap.prohibitions().update("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("test"), false));
            });
        }

        @Test
        void SubjectDoesNotExist() throws PMException {
            runTest(pap -> {
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
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
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
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
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createUserAttribute("subject2", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));
                pap.prohibitions().update("label", ProhibitionSubject.userAttribute("subject2"), new AccessRightSet("read", "write"), true,
                        new ContainerCondition("oa1", false),
                        new ContainerCondition("oa2", true));

                Prohibition p = pap.prohibitions().get("label");
                assertEquals("label", p.getLabel());
                assertEquals("subject2", p.getSubject().getName());
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
            void Success() throws PMException {
                runTest(pap -> {
                    pap.graph().createPolicyClass("pc1");
                    pap.graph().createUserAttribute("subject", "pc1");
                    pap.graph().createObjectAttribute("oa1", "pc1");
                    pap.graph().createObjectAttribute("oa2", "pc1");
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
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().createObjectAttribute("oa3", "pc1");
                pap.graph().createObjectAttribute("oa4", "pc1");

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
                    assertEquals("subject", p.getSubject().getName());
                    assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                    assertTrue(p.isIntersection());
                    assertEquals(2, p.getContainers().size());
                    assertEquals(List.of(
                            new ContainerCondition("oa1", true),
                            new ContainerCondition("oa2", false)
                    ), p.getContainers());
                } else if (p.getLabel().equals("label2")) {
                    assertEquals("label2", p.getLabel());
                    assertEquals("subject", p.getSubject().getName());
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
                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().createObjectAttribute("oa3", "pc1");
                pap.graph().createObjectAttribute("oa4", "pc1");
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

                pap.graph().createPolicyClass("pc1");
                pap.graph().createUserAttribute("subject", "pc1");
                pap.graph().createObjectAttribute("oa1", "pc1");
                pap.graph().createObjectAttribute("oa2", "pc1");
                pap.graph().createObjectAttribute("oa3", "pc1");
                pap.graph().createObjectAttribute("oa4", "pc1");
                pap.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.prohibitions().create("label1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));
                pap.prohibitions().create("label2", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa3", true),
                        new ContainerCondition("oa4", false));

                Prohibition p = pap.prohibitions().get("label1");
                assertEquals("label1", p.getLabel());
                assertEquals("subject", p.getSubject().getName());
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
                                        new CreatePolicyStatement(new Expression(new VariableReference("test_pc", Type.string())))
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
                                        new CreatePolicyStatement(new Expression(new VariableReference("test_pc", Type.string())))
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
                                        new CreatePolicyStatement(new Expression(new VariableReference("test_pc", Type.string())))
                                )
                        )
                );


        @Nested
        class CreateObligation {

            @Test
            void AuthorDoesNotExist() throws PMException {
                runTest(pap -> {
                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.obligations().create(new UserContext("u1"), obligation1.getLabel(),
                                    obligation1.getRules().toArray(Rule[]::new)));
                });
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

    @Nested
    class addFunction {

        FunctionDefinitionStatement testFunc = new FunctionDefinitionStatement(
                "testFunc",
                Type.string(),
                Arrays.asList(
                        new FormalArgument("arg1", Type.string()),
                        new FormalArgument("arg2", Type.array(Type.string()))
                ),
                Arrays.asList(
                        new CreatePolicyStatement(new Expression(new VariableReference("pc1", Type.string()))),
                        new CreateAttrStatement(
                                new Expression(new VariableReference("ua1", Type.string())),
                                UA,
                                new Expression(new Literal(new ArrayLiteral(new Expression[]{new Expression(new VariableReference("pc1", Type.string()))}, Type.string())))
                        ),
                        new CreateAttrStatement(
                                new Expression(new VariableReference("oa1", Type.string())),
                                OA,
                                new Expression(new Literal(new ArrayLiteral(new Expression[]{new Expression(new VariableReference("pc1", Type.string()))}, Type.string())))
                        )
                )
        );

        @Test
        void functionAlreadyDefined() throws PMException {
            runTest(pap -> {
                pap.userDefinedPML().addFunction(testFunc);
                assertThrows(FunctionAlreadyDefinedException.class, () -> pap.userDefinedPML().addFunction(testFunc));
            });
        }

        @Test
        void success() throws PMException {
            runTest(pap -> {
                pap.userDefinedPML().addFunction(testFunc);
                assertTrue(pap.userDefinedPML().getFunctions().containsKey(testFunc.getFunctionName()));
                FunctionDefinitionStatement actual = pap.userDefinedPML().getFunctions().get(testFunc.getFunctionName());
                assertEquals(testFunc, actual);
            });
        }
    }

    @Nested
    class RemoveFunction {

        @Test
        void functionDoesNotExistNoException() throws PMException {
            runTest(pap -> {
                assertDoesNotThrow(() -> pap.userDefinedPML().removeFunction("func"));
            });
        }

        @Test
        void success() throws PMException {
            runTest(pap -> {
                pap.userDefinedPML().addFunction(new FunctionDefinitionStatement("testFunc", Type.voidType(), List.of(), List.of()));
                assertTrue(pap.userDefinedPML().getFunctions().containsKey("testFunc"));
                pap.userDefinedPML().removeFunction("testFunc");
                assertFalse(pap.userDefinedPML().getFunctions().containsKey("testFunc"));
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
                pap.userDefinedPML().addFunction(testFunc1);
                pap.userDefinedPML().addFunction(testFunc2);

                Map<String, FunctionDefinitionStatement> functions = pap.userDefinedPML().getFunctions();
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
                pap.userDefinedPML().addConstant("const1", new Value("test"));
                assertThrows(ConstantAlreadyDefinedException.class, () -> pap.userDefinedPML().addConstant("const1", new Value("test")));
            });
        }

        @Test
        void success() throws PMException {
            Value expected = new Value("test");

            runTest(pap -> {
                pap.userDefinedPML().addConstant("const1", expected);
                assertTrue(pap.userDefinedPML().getConstants().containsKey("const1"));
                Value actual = pap.userDefinedPML().getConstants().get("const1");
                assertEquals(expected, actual);
            });
        }
    }

    @Nested
    class RemoveConstant {

        @Test
        void constantDoesNotExistNoException() throws PMException {
            runTest(pap -> {
                assertDoesNotThrow(() -> pap.userDefinedPML().removeConstant("const1"));
            });
        }

        @Test
        void success() throws PMException {
            runTest(pap -> {
                pap.userDefinedPML().addConstant("const1", new Value("test"));
                assertTrue(pap.userDefinedPML().getConstants().containsKey("const1"));
                pap.userDefinedPML().removeConstant("const1");
                assertFalse(pap.userDefinedPML().getConstants().containsKey("const1"));
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
                pap.userDefinedPML().addConstant("const1", const1);
                pap.userDefinedPML().addConstant("const2", const2);

                Map<String, Value> constants = pap.userDefinedPML().getConstants();
                assertTrue(constants.containsKey("const1"));
                Value actual = constants.get("const1");
                assertEquals(const1, actual);

                assertTrue(constants.containsKey("const2"));
                actual = constants.get("const2");
                assertEquals(const2, actual);
            });
        }
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