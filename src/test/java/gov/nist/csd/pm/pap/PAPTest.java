package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.mysql.MysqlPolicyStore;
import gov.nist.csd.pm.pap.mysql.MysqlTestEnv;
import gov.nist.csd.pm.policy.author.pal.model.expression.*;
import gov.nist.csd.pm.policy.author.pal.model.function.FormalArgument;
import gov.nist.csd.pm.policy.author.pal.statement.*;
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
            pap.setResourceAccessRights(arset);
            assertEquals(arset, pap.getResourceAccessRights());

            assertThrows(AdminAccessRightExistsException.class, () -> pap.setResourceAccessRights(new AccessRightSet(CREATE_POLICY_CLASS)));
        });
    }

    @Nested
    class CreatePolicyClassTest {
        @Test
        void NameAlreadyExists() throws PMException {
            runTest(pap -> assertThrows(NodeNameExistsException.class, () -> pap.createPolicyClass(SUPER_PC)));
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                String rep = SuperPolicy.pcRepObjectAttribute("pc1");
                assertTrue(pap.nodeExists("pc1"));
                assertTrue(pap.nodeExists(rep));
            });
        }
    }

    @Nested
    class CreateObjectAttribute {

        @Test
        void NameAlreadyExists() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                assertThrows(NodeNameExistsException.class,
                        () -> pap.createObjectAttribute("oa1", "pc1"));
            });
        }

        @Test
        void ParentDoesNotExist() throws PMException {
            runTest(pap -> {
                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.createObjectAttribute("oa1", "pc1"));

                pap.createPolicyClass("pc1");

                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.createObjectAttribute("oa1", "pc1", "pc2"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createObjectAttribute("oa1", SUPER_OA);
                pap.createObjectAttribute("oa2", toProperties("k", "v"), SUPER_OA, "oa1");

                assertTrue(pap.nodeExists("oa1"));
                assertTrue(pap.nodeExists("oa2"));
                assertEquals("v", pap.getNode("oa2").getProperties().get("k"));

                assertTrue(pap.getChildren(SUPER_OA).containsAll(List.of("oa1", "oa2")));
                assertTrue(pap.getParents("oa1").contains(SUPER_OA));
                assertTrue(pap.getChildren("oa1").contains("oa2"));
                assertTrue(pap.getParents("oa2").containsAll(List.of(SUPER_OA, "oa1")));
            });
        }
    }

    @Nested
    class CreateUserAttributeTest {

        @Test
        void NameAlreadyExists() throws PMException {
            runTest(pap -> assertThrows(NodeNameExistsException.class, () -> pap.createUserAttribute(SUPER_UA, "pc1")));
        }

        @Test
        void ParentDoesNotExist() throws PMException {
            runTest(pap -> {
                assertThrows(NodeDoesNotExistException.class, () -> pap.createUserAttribute("ua1", "pc1"));
                assertThrows(NodeDoesNotExistException.class, () -> pap.createUserAttribute("ua1", SUPER_UA, "pc1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createUserAttribute("ua1", SUPER_UA);
                pap.createUserAttribute("ua2", toProperties("k", "v"), SUPER_UA, "ua1");

                assertTrue(pap.nodeExists("ua1"));
                assertTrue(pap.nodeExists("ua2"));
                assertEquals("v", pap.getNode("ua2").getProperties().get("k"));

                assertTrue(pap.getChildren(SUPER_UA).containsAll(List.of("ua1", "ua2")));
                assertTrue(pap.getParents("ua1").contains(SUPER_UA));
                assertTrue(pap.getChildren("ua1").contains("ua2"));
                assertTrue(pap.getParents("ua2").containsAll(List.of(SUPER_UA, "ua1")));
            });
        }
    }

    @Nested
    class CreateObjectTest {

        @Test
        void NameAlreadyExists() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createObject("o1", "oa1");

                assertThrows(NodeNameExistsException.class,
                        () -> pap.createObject("o1", "oa1"));
            });
        }

        @Test
        void ParentDoesNotExist() throws PMException {
            runTest(pap -> {
                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.createObject("o1", "oa1"));

                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");

                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.createObject("o1", "oa1", "oa2"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createObject("o1", toProperties("k", "v"), SUPER_OA);

                assertTrue(pap.nodeExists("o1"));
                assertEquals("v", pap.getNode("o1").getProperties().get("k"));

                assertTrue(pap.getChildren(SUPER_OA).contains("o1"));
                assertEquals( List.of(SUPER_OA), pap.getParents("o1"));
                assertTrue(pap.getChildren(SUPER_OA).contains("o1"));
            });
        }
    }

    @Nested
    class CreateUserTest {

        @Test
        void NameAlreadyExists() throws PMException {
            runTest(pap -> assertThrows(NodeNameExistsException.class, () -> pap.createUser(SUPER_USER, "ua1")));
        }

        @Test
        void ParentDoesNotExist() throws PMException {
            runTest(pap -> {
                assertThrows(NodeDoesNotExistException.class, () -> pap.createUser("u1", "ua1"));
                assertThrows(NodeDoesNotExistException.class, () -> pap.createUser("u1", SUPER_UA, "ua1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createUser("u1", toProperties("k", "v"), SUPER_UA, SUPER_UA1);

                assertTrue(pap.nodeExists("u1"));
                assertEquals("v", pap.getNode("u1").getProperties().get("k"));

                assertTrue(pap.getChildren(SUPER_UA).contains("u1"));
                assertTrue(pap.getParents("u1").containsAll(List.of(SUPER_UA, SUPER_UA1)));
                assertTrue(pap.getChildren(SUPER_UA).contains("u1"));
                assertTrue(pap.getChildren(SUPER_UA1).contains("u1"));
            });
        }
    }

    @Nested
    class SetNodePropertiesTest {

        @Test
        void NodeDoesNotExist() throws PMException {
            runTest(pap -> assertThrows(NodeDoesNotExistException.class,
                    () -> pap.setNodeProperties("oa1", noprops())));
        }

        @Test
        void EmptyProperties() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.setNodeProperties("pc1", noprops());

                assertTrue(pap.getNode("pc1").getProperties().isEmpty());
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.setNodeProperties("pc1", toProperties("k", "v"));

                assertEquals("v", pap.getNode("pc1").getProperties().get("k"));
            });
        }
    }

    @Nested
    class DeleteNodeTest {

        @Test
        void NullNameOrNodeDoesNotExist() throws PMException {
            runTest(pap -> {
                assertDoesNotThrow(() -> pap.deleteNode(null));
                assertDoesNotThrow(() -> pap.deleteNode("pc1"));
            });
        }

        @Test
        void DeletePolicyClassHasChildren() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");

                assertThrows(NodeHasChildrenException.class,
                        () -> pap.deleteNode("pc1"));
            });
        }

        @Test
        void DeletePolicyClass() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.deleteNode("pc1");
                assertFalse(pap.nodeExists("pc1"));
                assertFalse(pap.nodeExists(SuperPolicy.pcRepObjectAttribute("pc1")));
            });
        }

        @Test
        void DeleteNodeHasChildren() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createObjectAttribute("oa2", "oa1");

                assertThrows(NodeHasChildrenException.class,
                        () -> pap.deleteNode("oa1"));
            });
        }

        @Test
        void DeleteNodeWithProhibitionsAndObligations() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("ua1", "pc1");
                pap.createUserAttribute("oa1", "pc1");
                pap.createProhibition("label", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet(), true, new ContainerCondition("oa1", true));
                pap.createObligation(new UserContext(SUPER_USER), "oblLabel",
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
                        () -> pap.deleteNode("ua1"));
                assertThrows(NodeReferencedInProhibitionException.class,
                        () -> pap.deleteNode("oa1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");

                pap.deleteNode("oa1");

                assertFalse(pap.nodeExists("oa1"));
            });
        }
    }

    @Nested
    class GetNodeTest {

        @Test
        void DoesNotExist() throws PMException {
            runTest(pap -> assertThrows(NodeDoesNotExistException.class, () -> pap.getNode("pc1")));
        }

        @Test
        void GetPolicyClass() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1", Properties.toProperties("k", "v"));

                Node pc1 = pap.getNode("pc1");

                assertEquals("pc1", pc1.getName());
                assertEquals(PC, pc1.getType());
                assertEquals("v", pc1.getProperties().get("k"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", Properties.toProperties("k", "v"), "pc1");

                Node oa1 = pap.getNode("oa1");

                assertEquals("oa1", oa1.getName());
                assertEquals(OA, oa1.getType());
                assertEquals("v", oa1.getProperties().get("k"));
            });
        }
    }

    @Test
    void testSearch() throws PMException {
        runTest(pap -> {
            pap.createPolicyClass("pc1");
            pap.createObjectAttribute("oa1", toProperties("namespace", "test"), "pc1");
            pap.createObjectAttribute("oa2", toProperties("key1", "value1"), "pc1");
            pap.createObjectAttribute("oa3", toProperties("key1", "value1", "key2", "value2"), "pc1");

            List<String> nodes = pap.search(OA, noprops());
            assertEquals(6, nodes.size());

            nodes = pap.search(ANY, toProperties("key1", "value1"));
            assertEquals(2, nodes.size());

            nodes = pap.search(ANY, toProperties("namespace", "test"));
            assertEquals(1, nodes.size());

            nodes = pap.search(OA, toProperties("namespace", "test"));
            assertEquals(1, nodes.size());
            nodes = pap.search(OA, toProperties("key1", "value1"));
            assertEquals(2, nodes.size());
            nodes = pap.search(OA, toProperties("key1", "*"));
            assertEquals(2, nodes.size());
            nodes = pap.search(OA, toProperties("key1", "value1", "key2", "value2"));
            assertEquals(1, nodes.size());
            nodes = pap.search(OA, toProperties("key1", "value1", "key2", "*"));
            assertEquals(1, nodes.size());
            nodes = pap.search(OA, toProperties("key1", "value1", "key2", "no_value"));
            assertEquals(0, nodes.size());
            nodes = pap.search(ANY, noprops());
            assertEquals(11, nodes.size());
        });
    }

    @Test
    void testGetPolicyClasses() throws PMException {
        runTest(pap -> {
            pap.createPolicyClass("pc1");
            pap.createPolicyClass("pc2");
            pap.createPolicyClass("pc3");

            assertTrue(pap.getPolicyClasses().containsAll(Arrays.asList(SUPER_PC, "pc1", "pc2", "pc3")));
        });
    }

    @Test
    void testNodeExists() throws PMException {
        runTest(pap -> {
            assertTrue(pap.nodeExists(SUPER_PC));
            assertTrue(pap.nodeExists(SUPER_UA));
            assertTrue(pap.nodeExists(SUPER_PC_REP));
            assertTrue(pap.nodeExists(SUPER_USER));
            assertFalse(pap.nodeExists("pc1"));
        });
    }

    @Nested
    class AssignTest {

        @Test
        void ChildNodeDoesNotExist() throws PMException {
            runTest(pap -> assertThrows(NodeDoesNotExistException.class,
                    () -> pap.assign("oa1", "pc1")));
        }

        @Test
        void ParentNodeDoesNotExist() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.assign("oa1", "oa2"));
            });
        }

        @Test
        void AssignmentExistsDoesNothing() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.assign("oa1", "pc1");
            });
        }

        @Test
        void InvalidAssignment() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createUserAttribute("ua1", "pc1");

                assertThrows(InvalidAssignmentException.class,
                        () -> pap.assign("ua1", "oa1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createObjectAttribute("oa2", "pc1");
                pap.assign("oa2", "oa1");
                assertTrue(pap.getParents("oa2").contains("oa1"));
                assertTrue(pap.getChildren("oa1").contains("oa2"));
            });
        }
    }

    @Nested
    class GetChildrenTest {

        @Test
        void NodeDoesNotExist() throws PMException {
            runTest(pap -> assertThrows(NodeDoesNotExistException.class,
                    () -> pap.getChildren("oa1")));
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createObjectAttribute("oa2", "pc1");
                pap.createObjectAttribute("oa3", "pc1");


                assertTrue(pap.getChildren("pc1").containsAll(List.of("oa1", "oa2", "oa3")));
            });
        }
    }

    @Nested
    class GetParentsTest {

        @Test
        void NodeDoesNotExist() throws PMException {
            runTest(pap -> assertThrows(NodeDoesNotExistException.class,
                    () -> pap.getParents("oa1")));
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createObjectAttribute("oa2", "pc1");
                pap.createObjectAttribute("oa3", "pc1");
                pap.createObject("o1", "oa1");
                pap.assign("o1", "oa2");
                pap.assign("o1", "oa3");

                assertTrue(pap.getParents("o1").containsAll(List.of("oa1", "oa2", "oa3")));
            });
        }
    }

    @Nested
    class DeassignTest {

        @Test
        void ChildNodeDoesNotExistDoesNothing() throws PMException {
            runTest(pap -> pap.deassign("oa1", "pc1"));
        }

        @Test
        void ParentNodeDoesNotExistDoesNothing() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.deassign("oa1", "oa2");
            });
        }

        @Test
        void AssignmentDoesNotExistDoesNothing() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createObjectAttribute("oa2", "pc1");
                pap.deassign("oa1", "oa2");
            });
        }

        @Test
        void DisconnectedNode() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");

                assertThrows(DisconnectedNodeException.class,
                        () -> pap.deassign("oa1", "pc1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createPolicyClass("pc2");
                pap.createObjectAttribute("oa1", "pc1", "pc2");
                pap.deassign("oa1", "pc1");
                assertEquals(List.of("pc2"), pap.getParents("oa1"));
                assertFalse(pap.getParents("oa1").contains("pc1"));
                assertFalse(pap.getChildren("pc1").contains("oa1"));
            });
        }

    }

    @Nested
    class AssociateTest {

        @Test
        void NodeDoesNotExist() throws PMException {
            runTest(pap -> {
                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.associate("ua1", "oa1", new AccessRightSet()));

                pap.createPolicyClass("pc1");
                pap.createUserAttribute("ua1", "pc1");

                assertThrows(NodeDoesNotExistException.class,
                        () -> pap.associate("ua1", "oa1", new AccessRightSet()));
            });
        }

        @Test
        void NodesAlreadyAssigned() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("ua1", "pc1");
                pap.createUserAttribute("ua2", "ua1");
                assertDoesNotThrow(() -> pap.associate("ua2", "ua1", new AccessRightSet()));
            });
        }

        @Test
        void UnknownResourceAccessRight() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("ua1", "pc1");
                pap.createObjectAttribute("oa1", "pc1");
                assertThrows(UnknownAccessRightException.class,
                        () -> pap.associate("ua1", "oa1", new AccessRightSet("read")));
                pap.setResourceAccessRights(new AccessRightSet("read"));
                assertThrows(UnknownAccessRightException.class,
                        () -> pap.associate("ua1", "oa1", new AccessRightSet("write")));
                assertDoesNotThrow(() -> pap.associate("ua1", "oa1", new AccessRightSet("read")));
                assertDoesNotThrow(() -> pap.associate("ua1", "oa1", new AccessRightSet(ALL_ACCESS_RIGHTS)));
                assertDoesNotThrow(() -> pap.associate("ua1", "oa1", new AccessRightSet(ALL_RESOURCE_ACCESS_RIGHTS)));
                assertDoesNotThrow(() -> pap.associate("ua1", "oa1", new AccessRightSet(ALL_ADMIN_ACCESS_RIGHTS)));
            });
        }

        @Test
        void InvalidAssociation() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("ua1", "pc1");
                pap.createUserAttribute("ua2", "ua1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createObjectAttribute("oa2", "pc1");

                assertThrows(InvalidAssociationException.class,
                        () -> pap.associate("ua2", "pc1", new AccessRightSet()));
                assertThrows(InvalidAssociationException.class,
                        () -> pap.associate("oa1", "oa2", new AccessRightSet()));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("ua1", "pc1");
                pap.createObjectAttribute("oa1", "pc1");

                pap.setResourceAccessRights(new AccessRightSet("read", "write"));
                pap.associate("ua1", "oa1", new AccessRightSet("read"));

                assertTrue(
                        pap.getAssociationsWithSource("ua1")
                                .contains(new Association("ua1", "oa1"))
                );
                assertTrue(
                        pap.getAssociationsWithTarget("oa1")
                                .contains(new Association("ua1", "oa1"))
                );
            });
        }

        @Test
        void OverwriteSuccess() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("ua1", "pc1");
                pap.createObjectAttribute("oa1", "pc1");

                pap.setResourceAccessRights(new AccessRightSet("read", "write"));
                pap.associate("ua1", "oa1", new AccessRightSet("read"));

                List<Association> assocs = pap.getAssociationsWithSource("ua1");
                Association assoc = assocs.get(0);
                assertEquals("ua1", assoc.getSource());
                assertEquals("oa1", assoc.getTarget());
                assertEquals(new AccessRightSet("read"), assoc.getAccessRightSet());

                pap.associate("ua1", "oa1", new AccessRightSet("read", "write"));

                assocs = pap.getAssociationsWithSource("ua1");
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
                assertDoesNotThrow(() -> pap.dissociate("ua1", "oa1"));

                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createUserAttribute("ua1", "pc1");

                assertDoesNotThrow(() -> pap.dissociate("ua1", "oa2"));
            });
        }

        @Test
        void AssociationDoesNotExistDoesNothing() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createUserAttribute("ua1", "pc1");

                assertDoesNotThrow(() -> pap.dissociate("ua1", "oa1"));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createUserAttribute("ua1", "pc1");
                pap.associate("ua1", "oa1", new AccessRightSet());

                pap.dissociate("ua1", "oa1");

                assertFalse(pap.getAssociationsWithSource("ua1")
                        .contains(new Association("ua1", "oa1")));
                assertFalse(pap.getAssociationsWithTarget("oa1")
                        .contains(new Association("ua1", "oa1")));
            });
        }
    }

    @Nested
    class GetAssociationsWithSourceTest {

        @Test
        void NodeDoesNotExist() throws PMException {
            runTest(pap -> assertThrows(NodeDoesNotExistException.class,
                    () -> pap.getAssociationsWithSource("ua1")));
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.setResourceAccessRights(new AccessRightSet("read", "write"));
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createObjectAttribute("oa2", "pc1");
                pap.createUserAttribute("ua1", "pc1");
                pap.associate("ua1", "oa1", new AccessRightSet("read"));
                pap.associate("ua1", "oa2", new AccessRightSet("read", "write"));

                List<Association> assocs = pap.getAssociationsWithSource("ua1");

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
                    () -> pap.getAssociationsWithTarget("oa1")));
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.setResourceAccessRights(new AccessRightSet("read", "write"));
                pap.createPolicyClass("pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createUserAttribute("ua1", "pc1");
                pap.createUserAttribute("ua2", "pc1");
                pap.associate("ua1", "oa1", new AccessRightSet("read"));
                pap.associate("ua2", "oa1", new AccessRightSet("read", "write"));

                List<Association> assocs = pap.getAssociationsWithTarget("oa1");

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
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("subject", "pc1");

                pap.createProhibition("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet(), false);

                assertThrows(ProhibitionExistsException.class,
                        () -> pap.createProhibition("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet(), false));
            });
        }

        @Test
        void InvalidAccessRights() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("subject", "pc1");

                assertThrows(UnknownAccessRightException.class,
                        () -> pap.createProhibition("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), false));
            });
        }

        @Test
        void ContainerDoesNotExist() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("subject", "pc1");
                pap.setResourceAccessRights(new AccessRightSet("read"));
                assertThrows(ProhibitionContainerDoesNotExistException.class,
                        () -> pap.createProhibition("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), false, new ContainerCondition("oa1", true)));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("subject", "pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createObjectAttribute("oa2", "pc1");
                pap.setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.createProhibition("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));

                Prohibition p = pap.getProhibition("label");
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
                    () -> pap.updateProhibition("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet(), false)));
        }

        @Test
        void InvalidAccessRights() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("subject", "pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.createProhibition("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true));

                assertThrows(UnknownAccessRightException.class,
                        () -> pap.updateProhibition("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("test"), false));
            });
        }

        @Test
        void SubjectDoesNotExist() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("subject", "pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.createProhibition("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true));

                assertThrows(ProhibitionSubjectDoesNotExistException.class,
                        () -> pap.updateProhibition("label", ProhibitionSubject.userAttribute("test"), new AccessRightSet("read"), false));
                assertDoesNotThrow(() -> pap.updateProhibition("label", ProhibitionSubject.process("subject"), new AccessRightSet("read"), false));
            });
        }

        @Test
        void ContainerDoesNotExist() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("subject", "pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.createProhibition("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true));

                assertThrows(ProhibitionContainerDoesNotExistException.class,
                        () -> pap.updateProhibition("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), false, new ContainerCondition("oa3", true)));
            });
        }

        @Test
        void Success() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("subject", "pc1");
                pap.createUserAttribute("subject2", "pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createObjectAttribute("oa2", "pc1");
                pap.setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.createProhibition("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));
                pap.updateProhibition("label", ProhibitionSubject.userAttribute("subject2"), new AccessRightSet("read", "write"), true,
                        new ContainerCondition("oa1", false),
                        new ContainerCondition("oa2", true));

                Prohibition p = pap.getProhibition("label");
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
                        () -> pap.updateProhibition("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet(), false)));
            }

            @Test
            void Success() throws PMException {
                runTest(pap -> {
                    pap.createPolicyClass("pc1");
                    pap.createUserAttribute("subject", "pc1");
                    pap.createObjectAttribute("oa1", "pc1");
                    pap.createObjectAttribute("oa2", "pc1");
                    pap.setResourceAccessRights(new AccessRightSet("read", "write"));

                    pap.createProhibition("label", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                            new ContainerCondition("oa1", true),
                            new ContainerCondition("oa2", false));

                    pap.deleteProhibition("label");

                    assertThrows(ProhibitionDoesNotExistException.class,
                            () -> pap.getProhibition("label"));
                });
            }
        }
    }

    @Nested
    class GetProhibitionsTest {

        @Test
        void GetProhibitions() throws PMException {
            runTest(pap -> {
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("subject", "pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createObjectAttribute("oa2", "pc1");
                pap.createObjectAttribute("oa3", "pc1");
                pap.createObjectAttribute("oa4", "pc1");

                pap.setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.createProhibition("label1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));
                pap.createProhibition("label2", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa3", true),
                        new ContainerCondition("oa4", false));

                Map<String, List<Prohibition>> prohibitions = pap.getProhibitions();
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
                pap.createPolicyClass("pc1");
                pap.createUserAttribute("subject", "pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createObjectAttribute("oa2", "pc1");
                pap.createObjectAttribute("oa3", "pc1");
                pap.createObjectAttribute("oa4", "pc1");
                pap.setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.createProhibition("label1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));
                pap.createProhibition("label2", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa3", true),
                        new ContainerCondition("oa4", false));

                List<Prohibition> prohibitions = pap.getProhibitionsWithSubject("subject");
                assertEquals(2, prohibitions.size());
                checkProhibitions(prohibitions);
            });
        }

        @Test
        void GetProhibition() throws PMException {
            runTest(pap -> {
                assertThrows(ProhibitionDoesNotExistException.class,
                        () -> pap.getProhibition("label"));

                pap.createPolicyClass("pc1");
                pap.createUserAttribute("subject", "pc1");
                pap.createObjectAttribute("oa1", "pc1");
                pap.createObjectAttribute("oa2", "pc1");
                pap.createObjectAttribute("oa3", "pc1");
                pap.createObjectAttribute("oa4", "pc1");
                pap.setResourceAccessRights(new AccessRightSet("read", "write"));

                pap.createProhibition("label1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));
                pap.createProhibition("label2", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa3", true),
                        new ContainerCondition("oa4", false));

                Prohibition p = pap.getProhibition("label1");
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
                            () -> pap.createObligation(new UserContext("u1"), obligation1.getLabel(),
                                    obligation1.getRules().toArray(Rule[]::new)));
                });
            }

            @Test
            void EventSubjectDoesNotExist() throws PMException {
                runTest(pap -> {
                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.createObligation(
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
                            () -> pap.createObligation(
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
                            () -> pap.createObligation(
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
                            () -> pap.createObligation(
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
                            () -> pap.createObligation(
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
                    pap.createObligation(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));

                    assertThrows(ObligationExistsException.class,
                            () -> pap.createObligation(obligation1.getAuthor(), obligation1.getLabel()));

                    Obligation actual = pap.getObligation(obligation1.getLabel());
                    assertEquals(obligation1, actual);
                });
            }
        }

        @Nested
        class UpdateObligation {

            @Test
            void AuthorDoesNotExist() throws PMException {
                runTest(pap -> {
                    pap.createObligation(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));

                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.updateObligation(new UserContext("u1"), obligation1.getLabel(),
                                    obligation1.getRules().toArray(Rule[]::new)));
                });
            }

            @Test
            void EventSubjectDoesNotExist() throws PMException {
                runTest(pap -> {
                    pap.createObligation(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));

                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.updateObligation(
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
                            () -> pap.updateObligation(
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
                    pap.createObligation(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));

                    assertThrows(NodeDoesNotExistException.class,
                            () -> pap.updateObligation(
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
                            () -> pap.updateObligation(
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
                            () -> pap.updateObligation(
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
                            () -> pap.updateObligation(new UserContext(SUPER_USER), obligation1.getLabel()));

                    pap.createObligation(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));

                    pap.updateObligation(new UserContext(SUPER_USER), obligation1.getLabel(),
                            obligation2.getRules().toArray(Rule[]::new));

                    Obligation expected = new Obligation(obligation1);
                    expected.setRules(obligation2.getRules());

                    Obligation actual = pap.getObligation(obligation1.getLabel());
                    assertEquals(expected, actual);
                });
            }

        }

        @Test
        void DeleteObligation() throws PMException {
            runTest(pap -> {
                assertDoesNotThrow(() -> pap.deleteObligation(obligation1.getLabel()));

                pap.createObligation(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));
                pap.createObligation(obligation2.getAuthor(), obligation2.getLabel(), obligation2.getRules().toArray(Rule[]::new));

                pap.deleteObligation(obligation1.getLabel());

                assertThrows(ObligationDoesNotExistException.class,
                        () -> pap.getObligation(obligation1.getLabel()));
            });
        }

        @Test
        void GetObligations() throws PMException {
            runTest(pap -> {
                pap.createObligation(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));
                pap.createObligation(obligation2.getAuthor(), obligation2.getLabel(), obligation2.getRules().toArray(Rule[]::new));

                List<Obligation> obligations = pap.getObligations();
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
                        () -> pap.getObligation(obligation1.getLabel()));

                pap.createObligation(obligation1.getAuthor(), obligation1.getLabel(), obligation1.getRules().toArray(Rule[]::new));
                pap.createObligation(obligation2.getAuthor(), obligation2.getLabel(), obligation2.getRules().toArray(Rule[]::new));

                Obligation obligation = pap.getObligation(obligation1.getLabel());
                assertEquals(obligation1, obligation);
            });
        }

    }

    @Test
    void testTx() throws PMException {
        runTest(pap -> {
            pap.beginTx();
            pap.createPolicyClass("pc1");
            pap.createObjectAttribute("oa1", "pc1");
            pap.createUserAttribute("ua1", "pc1");
            pap.associate("ua1", "oa1", new AccessRightSet());
            pap.commit();

            assertTrue(pap.nodeExists("pc1"));
            assertTrue(pap.nodeExists("oa1"));
            assertTrue(pap.nodeExists("ua1"));
            assertTrue(pap.getAssociationsWithSource("ua1").contains(new Association("ua1", "oa1")));

            pap.beginTx();
            pap.deleteNode("ua1");
            pap.rollback();
            assertTrue(pap.nodeExists("ua1"));
        });
    }

    @Nested
    class addPALFunction {

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
                                new Expression(new VariableReference("pc1", Type.string()))
                        ),
                        new CreateAttrStatement(
                                new Expression(new VariableReference("oa1", Type.string())),
                                OA,
                                new Expression(new VariableReference("pc1", Type.string()))
                        )
                )
        );

        @Test
        void functionAlreadyDefined() throws PMException {
            runTest(pap -> {
                pap.addPALFunction(testFunc);
                assertThrows(FunctionAlreadyDefinedException.class, () -> pap.addPALFunction(testFunc));
            });
        }

        @Test
        void success() throws PMException {
            runTest(pap -> {
                pap.addPALFunction(testFunc);
                assertTrue(pap.getPALFunctions().containsKey(testFunc.getFunctionName()));
                FunctionDefinitionStatement actual = pap.getPALFunctions().get(testFunc.getFunctionName());
                assertEquals(testFunc, actual);
            });
        }
    }

    @Nested
    class RemoveFunction {

        @Test
        void functionDoesNotExistNoException() throws PMException {
            runTest(pap -> {
                assertDoesNotThrow(() -> pap.removePALFunction("func"));
            });
        }

        @Test
        void success() throws PMException {
            runTest(pap -> {
                pap.addPALFunction(new FunctionDefinitionStatement("testFunc", Type.voidType(), List.of(), List.of()));
                assertTrue(pap.getPALFunctions().containsKey("testFunc"));
                pap.removePALFunction("testFunc");
                assertFalse(pap.getPALFunctions().containsKey("testFunc"));
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
                pap.addPALFunction(testFunc1);
                pap.addPALFunction(testFunc2);

                Map<String, FunctionDefinitionStatement> functions = pap.getPALFunctions();
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
                pap.addPALConstant("const1", new Value("test"));
                assertThrows(ConstantAlreadyDefinedException.class, () -> pap.addPALConstant("const1", new Value("test")));
            });
        }

        @Test
        void success() throws PMException {
            Value expected = new Value("test");

            runTest(pap -> {
                pap.addPALConstant("const1", expected);
                assertTrue(pap.getPALConstants().containsKey("const1"));
                Value actual = pap.getPALConstants().get("const1");
                assertEquals(expected, actual);
            });
        }
    }

    @Nested
    class RemoveConstant {

        @Test
        void constantDoesNotExistNoException() throws PMException {
            runTest(pap -> {
                assertDoesNotThrow(() -> pap.removePALConstant("const1"));
            });
        }

        @Test
        void success() throws PMException {
            runTest(pap -> {
                pap.addPALConstant("const1", new Value("test"));
                assertTrue(pap.getPALConstants().containsKey("const1"));
                pap.removePALConstant("const1");
                assertFalse(pap.getPALConstants().containsKey("const1"));
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
                pap.addPALConstant("const1", const1);
                pap.addPALConstant("const2", const2);

                Map<String, Value> constants = pap.getPALConstants();
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