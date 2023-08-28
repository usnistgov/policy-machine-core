package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.exceptions.*;
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
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.pml.model.expression.*;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.CreateAttrStatement;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.ALL_ADMIN_ACCESS_RIGHTS;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.toProperties;
import static org.junit.jupiter.api.Assertions.*;

public abstract class PolicyStoreTest {

    protected PolicyStore policyStore;

    public abstract PolicyStore getPolicyStore() throws PMException;

    @BeforeEach
    void setup() throws PMException {
        policyStore = getPolicyStore();
    }

    @Nested
    class GraphStoreTests {

        @Test
        void testSetResourceAccessRights() throws PMException {
            AccessRightSet arset = new AccessRightSet("read", "write");
            policyStore.graph().setResourceAccessRights(arset);
            assertEquals(arset, policyStore.graph().getResourceAccessRights());

            assertThrows(AdminAccessRightExistsException.class, () ->
                    policyStore.graph().setResourceAccessRights(new AccessRightSet(CREATE_POLICY_CLASS)));
        }

        @Test
        void testGetResourceAccessRights() throws PMException {
            AccessRightSet arset = new AccessRightSet("read", "write");
            policyStore.graph().setResourceAccessRights(arset);
            assertEquals(arset, policyStore.graph().getResourceAccessRights());
        }

        @Nested
        class CreatePolicyClassTest {
            @Test
            void NameAlreadyExists() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                assertDoesNotThrow(() -> policyStore.graph().createPolicyClass("pc2"));
                assertThrows(NodeNameExistsException.class, () -> policyStore.graph().createPolicyClass("pc1"));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                String rep = AdminPolicy.policyClassObjectAttributeName("pc1");
                assertTrue(policyStore.graph().nodeExists("pc1"));
                assertTrue(policyStore.graph().nodeExists(rep));
            }
        }

        @Nested
        class CreateObjectAttribute {

            @Test
            void NameAlreadyExists() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                assertThrows(NodeNameExistsException.class,
                        () -> policyStore.graph().createObjectAttribute("oa1", "pc1"));
            }

            @Test
            void ParentDoesNotExist() throws PMException {
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.graph().createObjectAttribute("oa1", "pc1"));

                policyStore.graph().createPolicyClass("pc1");

                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.graph().createObjectAttribute("oa1", "pc1", "pc2"));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createObjectAttribute("oa2", toProperties("k", "v"), "oa1");

                assertTrue(policyStore.graph().nodeExists("oa1"));
                assertTrue(policyStore.graph().nodeExists("oa2"));
                assertEquals("v", policyStore.graph().getNode("oa2").getProperties().get("k"));

                assertTrue(policyStore.graph().getChildren("pc1").contains("oa1"));
                assertTrue(policyStore.graph().getChildren("oa1").contains("oa2"));

                assertTrue(policyStore.graph().getParents("oa1").contains("pc1"));
                assertTrue(policyStore.graph().getParents("oa2").contains("oa1"));
            }
        }

        @Nested
        class CreateUserAttributeTest {

            @Test
            void NameAlreadyExists() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                assertDoesNotThrow(() -> policyStore.graph().createUserAttribute("ua1", "pc1"));
                assertThrows(NodeNameExistsException.class, () -> policyStore.graph().createUserAttribute("ua1", "pc1"));
            }

            @Test
            void ParentDoesNotExist() throws PMException {
                assertThrows(NodeDoesNotExistException.class, () -> policyStore.graph().createUserAttribute("ua1", "pc1"));
                policyStore.graph().createPolicyClass("pc1");
                assertThrows(NodeDoesNotExistException.class, () -> policyStore.graph().createUserAttribute("ua1", "pc1", "pc2"));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");
                policyStore.graph().createUserAttribute("ua2", toProperties("k", "v"), "ua1");

                assertTrue(policyStore.graph().nodeExists("ua1"));
                assertTrue(policyStore.graph().nodeExists("ua2"));
                assertEquals("v", policyStore.graph().getNode("ua2").getProperties().get("k"));

                assertTrue(policyStore.graph().getChildren("pc1").contains("ua1"));
                assertTrue(policyStore.graph().getChildren("ua1").contains("ua2"));
                assertTrue(policyStore.graph().getParents("ua2").contains("ua1"));
                assertTrue(policyStore.graph().getParents("ua1").contains("ua2"));
            }
        }

        @Nested
        class CreateObjectTest {

            @Test
            void NameAlreadyExists() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createObject("o1", "oa1");

                assertThrows(NodeNameExistsException.class,
                        () -> policyStore.graph().createObject("o1", "oa1"));
            }

            @Test
            void ParentDoesNotExist() throws PMException {
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.graph().createObject("o1", "oa1"));

                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");

                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.graph().createObject("o1", "oa1", "oa2"));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");

                policyStore.graph().createObject("o1", toProperties("k", "v"), "oa1");

                assertTrue(policyStore.graph().nodeExists("o1"));
                assertEquals("v", policyStore.graph().getNode("o1").getProperties().get("k"));

                assertTrue(policyStore.graph().getChildren("oa1").contains("o1"));
                assertEquals( List.of("oa1"), policyStore.graph().getParents("o1"));
                assertTrue(policyStore.graph().getChildren("oa1").contains("o1"));
            }
        }

        @Nested
        class CreateUserTest {

            @Test
            void NameAlreadyExists() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");
                policyStore.graph().createUser("u1", "ua1");
                assertDoesNotThrow(() -> policyStore.graph().createUser("u2", "ua1"));
                assertThrows(NodeNameExistsException.class, () -> policyStore.graph().createUser("u1", "ua1"));
            }

            @Test
            void ParentDoesNotExist() throws PMException {
                assertThrows(NodeDoesNotExistException.class, () -> policyStore.graph().createUser("u1", "ua1"));
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");
                assertThrows(NodeDoesNotExistException.class, () -> policyStore.graph().createUser("u1", "ua1", "ua2"));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("ua1", "pc2");
                policyStore.graph().createUserAttribute("ua2", "pc2");


                policyStore.graph().createUser("u1", toProperties("k", "v"), "ua1", "ua2");

                assertTrue(policyStore.graph().nodeExists("u1"));
                assertEquals("v", policyStore.graph().getNode("u1").getProperties().get("k"));

                assertTrue(policyStore.graph().getChildren("ua1").contains("u1"));
                assertTrue(policyStore.graph().getChildren("ua2").contains("u1"));
                assertTrue(policyStore.graph().getParents("u1").containsAll(List.of("ua1", "ua2")));
            }
        }

        @Nested
        class SetNodePropertiesTest {

            @Test
            void NodeDoesNotExist() throws PMException {
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.graph().setNodeProperties("oa1", NO_PROPERTIES));
            }

            @Test
            void EmptyProperties() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().setNodeProperties("pc1", NO_PROPERTIES);

                assertTrue(policyStore.graph().getNode("pc1").getProperties().isEmpty());
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().setNodeProperties("pc1", toProperties("k", "v"));

                assertEquals("v", policyStore.graph().getNode("pc1").getProperties().get("k"));
            }
        }

        @Nested
        class DeleteNodeTest {

            @Test
            void NullNameOrNodeDoesNotExist() throws PMException {
                assertDoesNotThrow(() -> policyStore.graph().deleteNode(null));
                assertDoesNotThrow(() -> policyStore.graph().deleteNode("pc1"));
            }

            @Test
            void DeletePolicyClassHasChildren() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");

                assertThrows(NodeHasChildrenException.class,
                        () -> policyStore.graph().deleteNode("pc1"));
            }

            @Test
            void DeletePolicyClass() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().deleteNode("pc1");
                assertFalse(policyStore.graph().nodeExists("pc1"));
                assertFalse(policyStore.graph().nodeExists(AdminPolicy.policyClassObjectAttributeName("pc1")));
            }

            @Test
            void DeleteNodeHasChildren() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createObjectAttribute("oa2", "oa1");

                assertThrows(NodeHasChildrenException.class,
                        () -> policyStore.graph().deleteNode("oa1"));
            }

            @Test
            void DeleteNodeWithProhibitionsAndObligations() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");
                policyStore.graph().createUserAttribute("u1", "ua1");
                policyStore.graph().createUserAttribute("oa1", "pc1");
                policyStore.prohibitions().create("pro1", ProhibitionSubject.userAttribute("ua1"), new AccessRightSet(), true, new ContainerCondition("oa1", true));
                policyStore.obligations().create(new UserContext("u1"), "oblLabel",
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
                        () -> policyStore.graph().deleteNode("ua1"));
                assertThrows(NodeReferencedInProhibitionException.class,
                        () -> policyStore.graph().deleteNode("oa1"));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");

                policyStore.graph().deleteNode("oa1");

                assertFalse(policyStore.graph().nodeExists("oa1"));
            }
        }

        @Nested
        class GetNodeTest {

            @Test
            void DoesNotExist() {
                assertThrows(NodeDoesNotExistException.class, () -> policyStore.graph().getNode("pc1"));
            }

            @Test
            void GetPolicyClass() throws PMException {
                policyStore.graph().createPolicyClass("pc1", Properties.toProperties("k", "v"));

                Node pc1 = policyStore.graph().getNode("pc1");

                assertEquals("pc1", pc1.getName());
                assertEquals(PC, pc1.getType());
                assertEquals("v", pc1.getProperties().get("k"));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", Properties.toProperties("k", "v"), "pc1");

                Node oa1 = policyStore.graph().getNode("oa1");

                assertEquals("oa1", oa1.getName());
                assertEquals(OA, oa1.getType());
                assertEquals("v", oa1.getProperties().get("k"));
            }
        }

        @Test
        void testSearch() throws PMException {
            policyStore.graph().createPolicyClass("pc1");
            policyStore.graph().createObjectAttribute("oa1", toProperties("namespace", "test"), "pc1");
            policyStore.graph().createObjectAttribute("oa2", toProperties("key1", "value1"), "pc1");
            policyStore.graph().createObjectAttribute("oa3", toProperties("key1", "value1", "key2", "value2"), "pc1");

            List<String> nodes = policyStore.graph().search(OA, NO_PROPERTIES);
            assertEquals(3, nodes.size());

            nodes = policyStore.graph().search(ANY, toProperties("key1", "value1"));
            assertEquals(2, nodes.size());

            nodes = policyStore.graph().search(ANY, toProperties("namespace", "test"));
            assertEquals(1, nodes.size());

            nodes = policyStore.graph().search(OA, toProperties("namespace", "test"));
            assertEquals(1, nodes.size());
            nodes = policyStore.graph().search(OA, toProperties("key1", "value1"));
            assertEquals(2, nodes.size());
            nodes = policyStore.graph().search(OA, toProperties("key1", "*"));
            assertEquals(2, nodes.size());
            nodes = policyStore.graph().search(OA, toProperties("key1", "value1", "key2", "value2"));
            assertEquals(1, nodes.size());
            nodes = policyStore.graph().search(OA, toProperties("key1", "value1", "key2", "*"));
            assertEquals(1, nodes.size());
            nodes = policyStore.graph().search(OA, toProperties("key1", "value1", "key2", "no_value"));
            assertEquals(0, nodes.size());
            nodes = policyStore.graph().search(ANY, NO_PROPERTIES);
            assertEquals(4, nodes.size());
        }

        @Test
        void testGetPolicyClasses() throws PMException {
            policyStore.graph().createPolicyClass("pc1");
            policyStore.graph().createPolicyClass("pc2");
            policyStore.graph().createPolicyClass("pc3");

            assertTrue(policyStore.graph().getPolicyClasses().containsAll(Arrays.asList("pc1", "pc2", "pc3")));
        }

        @Test
        void testNodeExists() throws PMException {
            policyStore.graph().createPolicyClass("pc1");
            policyStore.graph().createUserAttribute("ua1", "pc1");
            assertTrue(policyStore.graph().nodeExists("pc1"));
            assertTrue(policyStore.graph().nodeExists("ua1"));
            assertFalse(policyStore.graph().nodeExists("pc2"));
        }

        @Nested
        class AssignTest {

            @Test
            void ChildNodeDoesNotExist() throws PMException {
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.graph().assign("oa1", "pc1"));
            }

            @Test
            void ParentNodeDoesNotExist() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.graph().assign("oa1", "oa2"));
            }

            @Test
            void AssignmentExistsDoesNothing() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().assign("oa1", "pc1");
            }

            @Test
            void InvalidAssignment() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");

                assertThrows(InvalidAssignmentException.class,
                        () -> policyStore.graph().assign("ua1", "oa1"));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createObjectAttribute("oa2", "pc1");
                policyStore.graph().assign("oa2", "oa1");
                assertTrue(policyStore.graph().getParents("oa2").contains("oa1"));
                assertTrue(policyStore.graph().getChildren("oa1").contains("oa2"));
            }
        }

        @Nested
        class GetChildrenTest {

            @Test
            void NodeDoesNotExist() throws PMException {
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.graph().getChildren("oa1"));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createObjectAttribute("oa2", "pc1");
                policyStore.graph().createObjectAttribute("oa3", "pc1");


                assertTrue(policyStore.graph().getChildren("pc1").containsAll(List.of("oa1", "oa2", "oa3")));
            }
        }

        @Nested
        class GetParentsTest {

            @Test
            void NodeDoesNotExist() throws PMException {
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.graph().getParents("oa1"));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createObjectAttribute("oa2", "pc1");
                policyStore.graph().createObjectAttribute("oa3", "pc1");
                policyStore.graph().createObject("o1", "oa1");
                policyStore.graph().assign("o1", "oa2");
                policyStore.graph().assign("o1", "oa3");

                assertTrue(policyStore.graph().getParents("o1").containsAll(List.of("oa1", "oa2", "oa3")));
            }
        }

        @Nested
        class DeassignTest {

            @Test
            void ChildNodeDoesNotExistDoesNothing() throws PMException {
                policyStore.graph().deassign("oa1", "pc1");
            }

            @Test
            void ParentNodeDoesNotExistDoesNothing() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().deassign("oa1", "oa2");
            }

            @Test
            void AssignmentDoesNotExistDoesNothing() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createObjectAttribute("oa2", "pc1");
                policyStore.graph().deassign("oa1", "oa2");
            }

            @Test
            void DisconnectedNode() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");

                assertThrows(DisconnectedNodeException.class,
                        () -> policyStore.graph().deassign("oa1", "pc1"));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createPolicyClass("pc2");
                policyStore.graph().createObjectAttribute("oa1", "pc1", "pc2");
                policyStore.graph().deassign("oa1", "pc1");
                assertEquals(List.of("pc2"), policyStore.graph().getParents("oa1"));
                assertFalse(policyStore.graph().getParents("oa1").contains("pc1"));
                assertFalse(policyStore.graph().getChildren("pc1").contains("oa1"));
            }

        }

        @Nested
        class AssociateTest {

            @Test
            void NodeDoesNotExist() throws PMException {
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.graph().associate("ua1", "oa1", new AccessRightSet()));

                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");

                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.graph().associate("ua1", "oa1", new AccessRightSet()));
            }

            @Test
            void NodesAlreadyAssigned() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");
                policyStore.graph().createUserAttribute("ua2", "ua1");
                assertDoesNotThrow(() -> policyStore.graph().associate("ua2", "ua1", new AccessRightSet()));
            }

            @Test
            void UnknownResourceAccessRight() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                assertThrows(UnknownAccessRightException.class,
                        () -> policyStore.graph().associate("ua1", "oa1", new AccessRightSet("read")));
                policyStore.graph().setResourceAccessRights(new AccessRightSet("read"));
                assertThrows(UnknownAccessRightException.class,
                        () -> policyStore.graph().associate("ua1", "oa1", new AccessRightSet("write")));
                assertDoesNotThrow(() -> policyStore.graph().associate("ua1", "oa1", new AccessRightSet("read")));
                assertDoesNotThrow(() -> policyStore.graph().associate("ua1", "oa1", new AccessRightSet(ALL_ACCESS_RIGHTS)));
                assertDoesNotThrow(() -> policyStore.graph().associate("ua1", "oa1", new AccessRightSet(ALL_RESOURCE_ACCESS_RIGHTS)));
                assertDoesNotThrow(() -> policyStore.graph().associate("ua1", "oa1", new AccessRightSet(ALL_ADMIN_ACCESS_RIGHTS)));
            }

            @Test
            void InvalidAssociation() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");
                policyStore.graph().createUserAttribute("ua2", "ua1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createObjectAttribute("oa2", "pc1");

                assertThrows(InvalidAssociationException.class,
                        () -> policyStore.graph().associate("ua2", "pc1", new AccessRightSet()));
                assertThrows(InvalidAssociationException.class,
                        () -> policyStore.graph().associate("oa1", "oa2", new AccessRightSet()));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");

                policyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
                policyStore.graph().associate("ua1", "oa1", new AccessRightSet("read"));

                assertTrue(
                        policyStore.graph().getAssociationsWithSource("ua1")
                                .contains(new Association("ua1", "oa1"))
                );
                assertTrue(
                        policyStore.graph().getAssociationsWithTarget("oa1")
                                .contains(new Association("ua1", "oa1"))
                );
            }

            @Test
            void OverwriteSuccess() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");

                policyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
                policyStore.graph().associate("ua1", "oa1", new AccessRightSet("read"));

                List<Association> assocs = policyStore.graph().getAssociationsWithSource("ua1");
                Association assoc = assocs.get(0);
                assertEquals("ua1", assoc.getSource());
                assertEquals("oa1", assoc.getTarget());
                assertEquals(new AccessRightSet("read"), assoc.getAccessRightSet());

                policyStore.graph().associate("ua1", "oa1", new AccessRightSet("read", "write"));

                assocs = policyStore.graph().getAssociationsWithSource("ua1");
                assoc = assocs.get(0);
                assertEquals("ua1", assoc.getSource());
                assertEquals("oa1", assoc.getTarget());
                assertEquals(new AccessRightSet("read", "write"), assoc.getAccessRightSet());
            }
        }

        @Nested
        class DissociateTest {

            @Test
            void NodeDoesNotExistDoesNothing() throws PMException {
                assertDoesNotThrow(() -> policyStore.graph().dissociate("ua1", "oa1"));

                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");

                assertDoesNotThrow(() -> policyStore.graph().dissociate("ua1", "oa2"));
            }

            @Test
            void AssociationDoesNotExistDoesNothing() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");

                assertDoesNotThrow(() -> policyStore.graph().dissociate("ua1", "oa1"));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");
                policyStore.graph().associate("ua1", "oa1", new AccessRightSet());

                policyStore.graph().dissociate("ua1", "oa1");

                assertFalse(policyStore.graph().getAssociationsWithSource("ua1")
                        .contains(new Association("ua1", "oa1")));
                assertFalse(policyStore.graph().getAssociationsWithTarget("oa1")
                        .contains(new Association("ua1", "oa1")));
            }
        }

        @Nested
        class GetAssociationsWithSourceTest {

            @Test
            void NodeDoesNotExist() throws PMException {
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.graph().getAssociationsWithSource("ua1"));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createObjectAttribute("oa2", "pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");
                policyStore.graph().associate("ua1", "oa1", new AccessRightSet("read"));
                policyStore.graph().associate("ua1", "oa2", new AccessRightSet("read", "write"));

                List<Association> assocs = policyStore.graph().getAssociationsWithSource("ua1");

                assertEquals(2, assocs.size());

                for (Association assoc : assocs) {
                    checkAssociation(assoc);
                }
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
            void NodeDoesNotExist() {
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.graph().getAssociationsWithTarget("oa1"));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createUserAttribute("ua1", "pc1");
                policyStore.graph().createUserAttribute("ua2", "pc1");
                policyStore.graph().associate("ua1", "oa1", new AccessRightSet("read"));
                policyStore.graph().associate("ua2", "oa1", new AccessRightSet("read", "write"));

                List<Association> assocs = policyStore.graph().getAssociationsWithTarget("oa1");

                assertEquals(2, assocs.size());

                for (Association assoc : assocs) {
                    checkAssociation(assoc);
                }
            }

            private void checkAssociation(Association association) {
                if (association.getSource().equals("ua1")) {
                    assertEquals(new AccessRightSet("read"), association.getAccessRightSet());
                } else if (association.getSource().equals("ua2")) {
                    assertEquals(new AccessRightSet("read", "write"), association.getAccessRightSet());
                }
            }
        }

        @Test
        void testAssignAll() throws PMException {
            String pml = """
                create pc 'pc1'
                create oa 'oa1' in ['pc1']
                create oa 'oa2' in ['pc1']
                create ua 'ua1' in ['pc1']
                create u 'u1' in ['ua1']
                                    
                for i in range [1, 10] {
                    let name = concat(["o", numToStr(i)])
                    create object name in ['oa1']
                }
                """;
            policyStore.deserialize().fromPML(new UserContext("u1"), pml);

            List<String> children = policyStore.graph().getChildren("oa1");
            policyStore.graph().assignAll(children, "oa2");

            assertEquals(10, policyStore.graph().getChildren("oa2").size());

            assertDoesNotThrow(() -> {
                policyStore.graph().assignAll(children, "oa2");

                // reset policy
                policyStore.deserialize().fromPML(new UserContext("u1"), pml);

                // test with illegal assignment
                children.add("ua1");
                assertThrows(PMException.class, () -> {
                    policyStore.graph().assignAll(children, "oa2");
                    assertTrue(policyStore.graph().getChildren("oa2").isEmpty());

                    // test non existing target
                    assertThrows(PMException.class, () -> {
                        policyStore.graph().assignAll(children, "oa3");

                        // test non existing child
                        assertThrows(PMException.class, () -> {
                            children.remove("ua1");
                            children.add("oDNE");
                            policyStore.graph().assignAll(children, "oa2");
                        });
                    });
                });
            });
        }

        @Test
        void testDeassignAll() throws PMException {
            String pml = """
                                create pc 'pc1'
                                create oa 'oa1' in ['pc1']
                                create oa 'oa2' in ['pc1']
                                create ua 'ua1' in ['pc1']
                                create u 'u1' in ['ua1']
                                                    
                                for i in range [1, 10] {
                                    let name = concat(["o", numToStr(i)])
                                    create object name in ['oa1']
                                }
                                                    
                                for i in range [1, 5] {
                                    let name = concat(["o", numToStr(i)])
                                    assign name to ['oa2']
                                }
                                """;
            policyStore.deserialize().fromPML(new UserContext("u1"), pml);

            List<String> toDelete = new ArrayList<>(List.of("o1", "o2", "o3", "o4", "o5"));
            policyStore.graph().deassignAll(toDelete, "oa1");
            assertEquals(5, policyStore.graph().getChildren("oa1").size());

            toDelete.clear();
            toDelete.add("oDNE");
            assertThrows(PMException.class, () -> {
                policyStore.graph().deassignAll(toDelete, "oa2");

                toDelete.clear();
                toDelete.add("o9");
                assertDoesNotThrow(() -> {
                    policyStore.graph().deassignAll(toDelete, "oa2");
                });
            });
        }

        @Test
        void testDeassignAllAndDelete() throws PMException {
            String pml = """
            create pc 'pc1'
            create oa 'oa1' in ['pc1']
            create oa 'oa2' in ['pc1']
            create ua 'ua1' in ['pc1']
            create u 'u1' in ['ua1']
                                
            for i in range [1, 10] {
                let name = concat(["o", numToStr(i)])
                create object name in ['oa1']
            }
                                
            for i in range [1, 5] {
                let name = concat(["o", numToStr(i)])
                assign name to ['oa2']
            }
            """;
            policyStore.deserialize().fromPML(new UserContext("u1"), pml);

            assertThrows(PMException.class, () -> {
                policyStore.graph().deassignAllFromAndDelete("oa1");

                policyStore.graph().assignAll(policyStore.graph().getChildren("oa1"), "oa2");

                policyStore.graph().deassignAllFromAndDelete("oa1");
                assertFalse(policyStore.graph().nodeExists("oa1"));
            });
        }

    }

    @Nested
    class ProhibitionsStoreTests {
        @Nested
        class CreateProhibitionTest {

            @Test
            void ProhibitionExists() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("subject", "pc1");

                policyStore.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet(), false);

                assertThrows(ProhibitionExistsException.class,
                        () -> policyStore.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet(), false));
            }

            @Test
            void InvalidAccessRights() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("subject", "pc1");

                assertThrows(UnknownAccessRightException.class,
                        () -> policyStore.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), false));
            }

            @Test
            void ContainerDoesNotExist() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("subject", "pc1");
                policyStore.graph().setResourceAccessRights(new AccessRightSet("read"));
                assertThrows(ProhibitionContainerDoesNotExistException.class,
                        () -> policyStore.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), false, new ContainerCondition("oa1", true)));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("subject", "pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createObjectAttribute("oa2", "pc1");
                policyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                policyStore.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));

                Prohibition p = policyStore.prohibitions().get("pro1");
                assertEquals("pro1", p.getName());
                assertEquals("subject", p.getSubject().getName());
                assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                assertTrue(p.isIntersection());
                assertEquals(2, p.getContainers().size());
                assertEquals(List.of(
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false)
                ), p.getContainers());
            }
        }

        @Nested
        class UpdateProhibitionTest {

            @Test
            void InvalidAccessRights() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("subject", "pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                policyStore.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true));

                assertThrows(UnknownAccessRightException.class,
                        () -> policyStore.prohibitions().update("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("test"), false));
            }

            @Test
            void SubjectDoesNotExist() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("subject", "pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                policyStore.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true));

                assertThrows(ProhibitionSubjectDoesNotExistException.class,
                        () -> policyStore.prohibitions().update("pro1", ProhibitionSubject.userAttribute("test"), new AccessRightSet("read"), false));
                assertDoesNotThrow(() -> policyStore.prohibitions().update("pro1", ProhibitionSubject.process("subject"), new AccessRightSet("read"), false));
            }

            @Test
            void ContainerDoesNotExist() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("subject", "pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                policyStore.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true));

                assertThrows(ProhibitionContainerDoesNotExistException.class,
                        () -> policyStore.prohibitions().update("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), false, new ContainerCondition("oa3", true)));
            }

            @Test
            void Success() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("subject", "pc1");
                policyStore.graph().createUserAttribute("subject2", "pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createObjectAttribute("oa2", "pc1");
                policyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                policyStore.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));
                policyStore.prohibitions().update("pro1", ProhibitionSubject.userAttribute("subject2"), new AccessRightSet("read", "write"), true,
                        new ContainerCondition("oa1", false),
                        new ContainerCondition("oa2", true));

                Prohibition p = policyStore.prohibitions().get("pro1");
                assertEquals("pro1", p.getName());
                assertEquals("subject2", p.getSubject().getName());
                assertEquals(new AccessRightSet("read", "write"), p.getAccessRightSet());
                assertTrue(p.isIntersection());
                assertEquals(2, p.getContainers().size());
                assertEquals(List.of(
                        new ContainerCondition("oa1", false),
                        new ContainerCondition("oa2", true)
                ), p.getContainers());
            }

            @Nested
            class DeleteProhibitionTest {

                @Test
                void Success() throws PMException {
                    policyStore.graph().createPolicyClass("pc1");
                    policyStore.graph().createUserAttribute("subject", "pc1");
                    policyStore.graph().createObjectAttribute("oa1", "pc1");
                    policyStore.graph().createObjectAttribute("oa2", "pc1");
                    policyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                    policyStore.prohibitions().create("pro1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                            new ContainerCondition("oa1", true),
                            new ContainerCondition("oa2", false));

                    policyStore.prohibitions().delete("pro1");

                    assertThrows(ProhibitionDoesNotExistException.class,
                            () -> policyStore.prohibitions().get("pro1"));
                }
            }
        }

        @Nested
        class GetProhibitionsTest {

            @Test
            void GetProhibitions() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("subject", "pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createObjectAttribute("oa2", "pc1");
                policyStore.graph().createObjectAttribute("oa3", "pc1");
                policyStore.graph().createObjectAttribute("oa4", "pc1");

                policyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                policyStore.prohibitions().create("label1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));
                policyStore.prohibitions().create("label2", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa3", true),
                        new ContainerCondition("oa4", false));

                Map<String, List<Prohibition>> prohibitions = policyStore.prohibitions().getAll();
                assertEquals(1, prohibitions.size());
                assertEquals(2, prohibitions.get("subject").size());
                checkProhibitions(prohibitions.get("subject"));
            }

            private void checkProhibitions(List<Prohibition> prohibitions) {
                for (Prohibition p : prohibitions) {
                    if (p.getName().equals("label1")) {
                        assertEquals("label1", p.getName());
                        assertEquals("subject", p.getSubject().getName());
                        assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                        assertTrue(p.isIntersection());
                        assertEquals(2, p.getContainers().size());
                        assertEquals(List.of(
                                new ContainerCondition("oa1", true),
                                new ContainerCondition("oa2", false)
                        ), p.getContainers());
                    } else if (p.getName().equals("label2")) {
                        assertEquals("label2", p.getName());
                        assertEquals("subject", p.getSubject().getName());
                        assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                        assertTrue(p.isIntersection());
                        assertEquals(2, p.getContainers().size());
                        assertEquals(List.of(
                                new ContainerCondition("oa3", true),
                                new ContainerCondition("oa4", false)
                        ), p.getContainers());
                    } else {
                        fail("unexpected prohibition label " + p.getName());
                    }
                }
            }

            @Test
            void GetProhibitionsFor() throws PMException {
                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("subject", "pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createObjectAttribute("oa2", "pc1");
                policyStore.graph().createObjectAttribute("oa3", "pc1");
                policyStore.graph().createObjectAttribute("oa4", "pc1");
                policyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                policyStore.prohibitions().create("label1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));
                policyStore.prohibitions().create("label2", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa3", true),
                        new ContainerCondition("oa4", false));

                List<Prohibition> prohibitions = policyStore.prohibitions().getWithSubject("subject");
                assertEquals(2, prohibitions.size());
                checkProhibitions(prohibitions);
            }

            @Test
            void GetProhibition() throws PMException {
                assertThrows(ProhibitionDoesNotExistException.class,
                        () -> policyStore.prohibitions().get("pro1"));

                policyStore.graph().createPolicyClass("pc1");
                policyStore.graph().createUserAttribute("subject", "pc1");
                policyStore.graph().createObjectAttribute("oa1", "pc1");
                policyStore.graph().createObjectAttribute("oa2", "pc1");
                policyStore.graph().createObjectAttribute("oa3", "pc1");
                policyStore.graph().createObjectAttribute("oa4", "pc1");
                policyStore.graph().setResourceAccessRights(new AccessRightSet("read", "write"));

                policyStore.prohibitions().create("label1", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false));
                policyStore.prohibitions().create("label2", ProhibitionSubject.userAttribute("subject"), new AccessRightSet("read"), true,
                        new ContainerCondition("oa3", true),
                        new ContainerCondition("oa4", false));

                Prohibition p = policyStore.prohibitions().get("label1");
                assertEquals("label1", p.getName());
                assertEquals("subject", p.getSubject().getName());
                assertEquals(new AccessRightSet("read"), p.getAccessRightSet());
                assertTrue(p.isIntersection());
                assertEquals(2, p.getContainers().size());
                assertEquals(List.of(
                        new ContainerCondition("oa1", true),
                        new ContainerCondition("oa2", false)
                ), p.getContainers());
            }
        }
    }

    @Nested
    class ObligationsStoreTests {

        Obligation obligation1 = new Obligation(
                new UserContext("u1"),
                "obl1",
                List.of(
                        new Rule(
                                "rule1",
                                new EventPattern(
                                        EventSubject.anyUser(),
                                        new Performs("test_event")
                                ),
                                new Response(
                                        new UserContext("u1"),
                                        new CreatePolicyStatement(new Expression(new VariableReference("test_pc", Type.string())))
                                )
                        )
                )
        );

        Obligation obligation2 = new Obligation(
                new UserContext("u1"),
                "label2")
                .addRule(
                        new Rule(
                                "rule1",
                                new EventPattern(
                                        EventSubject.anyUser(),
                                        new Performs("test_event")
                                ),
                                new Response(
                                        new UserContext("u1"),
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
                                        new UserContext("u1"),
                                        new CreatePolicyStatement(new Expression(new VariableReference("test_pc", Type.string())))
                                )
                        )
                );


        @Nested
        class CreateObligation {

            @Test
            void AuthorDoesNotExist() throws PMException {
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.obligations().create(new UserContext("u1"), obligation1.getName(),
                                obligation1.getRules().toArray(Rule[]::new)));
            }

            @Test
            void EventSubjectDoesNotExist() throws PMException {
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.obligations().create(
                                new UserContext("u1"),
                                "obl1",
                                new Rule(
                                        "rule1",
                                        new EventPattern(
                                                EventSubject.users("u1"),
                                                Performs.events("test_event"),
                                                Target.anyPolicyElement()
                                        ),
                                        new Response(new UserContext("u1"))
                                )
                        ));
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.obligations().create(
                                new UserContext("u1"),
                                "obl1",
                                new Rule(
                                        "rule1",
                                        new EventPattern(
                                                EventSubject.anyUserWithAttribute("ua1"),
                                                Performs.events("test_event"),
                                                Target.anyPolicyElement()
                                        ),
                                        new Response(new UserContext("u1"))
                                )
                        ));
            }

            @Test
            void EventTargetDoesNotExist() throws PMException {
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.obligations().create(
                                new UserContext("u1"),
                                "obl1",
                                new Rule(
                                        "rule1",
                                        new EventPattern(
                                                EventSubject.users("u1"),
                                                Performs.events("test_event"),
                                                Target.anyOfSet("oa1")
                                        ),
                                        new Response(new UserContext("u1"))
                                )
                        ));
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.obligations().create(
                                new UserContext("u1"),
                                "obl1",
                                new Rule(
                                        "rule1",
                                        new EventPattern(
                                                EventSubject.users("u1"),
                                                Performs.events("test_event"),
                                                Target.policyElement("oa1")
                                        ),
                                        new Response(new UserContext("u1"))
                                )
                        ));
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.obligations().create(
                                new UserContext("u1"),
                                "obl1",
                                new Rule(
                                        "rule1",
                                        new EventPattern(
                                                EventSubject.users("u1"),
                                                Performs.events("test_event"),
                                                Target.anyContainedIn("oa1")
                                        ),
                                        new Response(new UserContext("u1"))
                                )
                        ));
            }

            @Test
            void Success() throws PMException {
                policyStore.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));

                assertThrows(ObligationNameExistsException.class,
                        () -> policyStore.obligations().create(obligation1.getAuthor(), obligation1.getName()));

                Obligation actual = policyStore.obligations().get(obligation1.getName());
                assertEquals(obligation1, actual);
            }
        }

        @Nested
        class UpdateObligation {

            @Test
            void AuthorDoesNotExist() throws PMException {
                policyStore.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));

                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.obligations().update(new UserContext("u1"), obligation1.getName(),
                                obligation1.getRules().toArray(Rule[]::new)));
            }

            @Test
            void EventSubjectDoesNotExist() throws PMException {
                policyStore.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));

                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.obligations().update(
                                new UserContext("u1"),
                                obligation1.getName(),
                                new Rule(
                                        "rule1",
                                        new EventPattern(
                                                EventSubject.users("u1"),
                                                Performs.events("test_event"),
                                                Target.anyPolicyElement()
                                        ),
                                        new Response(new UserContext("u1"))
                                )
                        ));
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.obligations().update(
                                new UserContext("u1"),
                                obligation1.getName(),
                                new Rule(
                                        "rule1",
                                        new EventPattern(
                                                EventSubject.anyUserWithAttribute("ua1"),
                                                Performs.events("test_event"),
                                                Target.anyPolicyElement()
                                        ),
                                        new Response(new UserContext("u1"))
                                )
                        ));
            }

            @Test
            void EventTargetDoesNotExist() throws PMException {
                policyStore.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));

                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.obligations().update(
                                new UserContext("u1"),
                                obligation1.getName(),
                                new Rule(
                                        "rule1",
                                        new EventPattern(
                                                EventSubject.users("u1"),
                                                Performs.events("test_event"),
                                                Target.anyOfSet("oa1")
                                        ),
                                        new Response(new UserContext("u1"))
                                )
                        ));
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.obligations().update(
                                new UserContext("u1"),
                                obligation1.getName(),
                                new Rule(
                                        "rule1",
                                        new EventPattern(
                                                EventSubject.users("u1"),
                                                Performs.events("test_event"),
                                                Target.policyElement("oa1")
                                        ),
                                        new Response(new UserContext("u1"))
                                )
                        ));
                assertThrows(NodeDoesNotExistException.class,
                        () -> policyStore.obligations().update(
                                new UserContext("u1"),
                                obligation1.getName(),
                                new Rule(
                                        "rule1",
                                        new EventPattern(
                                                EventSubject.users("u1"),
                                                Performs.events("test_event"),
                                                Target.anyContainedIn("oa1")
                                        ),
                                        new Response(new UserContext("u1"))
                                )
                        ));
            }

            @Test
            void Success() throws PMException {
                assertThrows(ObligationDoesNotExistException.class,
                        () -> policyStore.obligations().update(new UserContext("u1"), obligation1.getName()));

                policyStore.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));

                policyStore.obligations().update(new UserContext("u1"), obligation1.getName(),
                        obligation2.getRules().toArray(Rule[]::new));

                Obligation expected = new Obligation(obligation1);
                expected.setRules(obligation2.getRules());

                Obligation actual = policyStore.obligations().get(obligation1.getName());
                assertEquals(expected, actual);
            }

        }

        @Test
        void DeleteObligation() throws PMException {
            assertDoesNotThrow(() -> policyStore.obligations().delete(obligation1.getName()));

            policyStore.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));
            policyStore.obligations().create(obligation2.getAuthor(), obligation2.getName(), obligation2.getRules().toArray(Rule[]::new));

            policyStore.obligations().delete(obligation1.getName());

            assertThrows(ObligationDoesNotExistException.class,
                    () -> policyStore.obligations().get(obligation1.getName()));
        }

        @Test
        void GetObligations() throws PMException {
            policyStore.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));
            policyStore.obligations().create(obligation2.getAuthor(), obligation2.getName(), obligation2.getRules().toArray(Rule[]::new));

            List<Obligation> obligations = policyStore.obligations().getAll();
            assertEquals(2, obligations.size());
            for (Obligation obligation : obligations) {
                if (obligation.getName().equals(obligation1.getName())) {
                    assertEquals(obligation1, obligation);
                } else {
                    assertEquals(obligation2, obligation);
                }
            }
        }

        @Test
        void GetObligation() throws PMException {
            assertThrows(ObligationDoesNotExistException.class,
                    () -> policyStore.obligations().get(obligation1.getName()));

            policyStore.obligations().create(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules().toArray(Rule[]::new));
            policyStore.obligations().create(obligation2.getAuthor(), obligation2.getName(), obligation2.getRules().toArray(Rule[]::new));

            Obligation obligation = policyStore.obligations().get(obligation1.getName());
            assertEquals(obligation1, obligation);
        }

    }

    @Nested
    class UserDefinedPMLStoreTests {

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
                policyStore.userDefinedPML().createFunction(testFunc);
                assertThrows(PMLFunctionAlreadyDefinedException.class, () -> policyStore.userDefinedPML().createFunction(testFunc));
            }

            @Test
            void success() throws PMException {
                policyStore.userDefinedPML().createFunction(testFunc);
                assertTrue(policyStore.userDefinedPML().getFunctions().containsKey(testFunc.getFunctionName()));
                FunctionDefinitionStatement actual = policyStore.userDefinedPML().getFunctions().get(testFunc.getFunctionName());
                assertEquals(testFunc, actual);
            }
        }

        @Nested
        class RemoveFunction {

            @Test
            void functionDoesNotExistNoException() throws PMException {
                assertDoesNotThrow(() -> policyStore.userDefinedPML().deleteFunction("func"));
            }

            @Test
            void success() throws PMException {
                policyStore.userDefinedPML().createFunction(new FunctionDefinitionStatement("testFunc", Type.voidType(), List.of(), List.of()));
                assertTrue(policyStore.userDefinedPML().getFunctions().containsKey("testFunc"));
                policyStore.userDefinedPML().deleteFunction("testFunc");
                assertFalse(policyStore.userDefinedPML().getFunctions().containsKey("testFunc"));
            }
        }

        @Nested
        class GetFunctions {

            @Test
            void success() throws PMException {
                FunctionDefinitionStatement testFunc1 = new FunctionDefinitionStatement("testFunc1", Type.voidType(), List.of(), List.of());
                FunctionDefinitionStatement testFunc2 = new FunctionDefinitionStatement("testFunc2", Type.voidType(), List.of(), List.of());

                policyStore.userDefinedPML().createFunction(testFunc1);
                policyStore.userDefinedPML().createFunction(testFunc2);

                Map<String, FunctionDefinitionStatement> functions = policyStore.userDefinedPML().getFunctions();
                assertTrue(functions.containsKey("testFunc1"));
                FunctionDefinitionStatement actual = functions.get("testFunc1");
                assertEquals(testFunc1, actual);

                assertTrue(functions.containsKey("testFunc2"));
                actual = functions.get("testFunc2");
                assertEquals(testFunc2, actual);
            }

        }

        @Nested
        class AddConstant {

            @Test
            void constantAlreadyDefined() throws PMException {
                policyStore.userDefinedPML().createConstant("const1", new Value("test"));
                assertThrows(PMLConstantAlreadyDefinedException.class, () -> policyStore.userDefinedPML().createConstant("const1", new Value("test")));
            }

            @Test
            void success() throws PMException {
                Value expected = new Value("test");

                policyStore.userDefinedPML().createConstant("const1", expected);
                assertTrue(policyStore.userDefinedPML().getConstants().containsKey("const1"));
                Value actual = policyStore.userDefinedPML().getConstants().get("const1");
                assertEquals(expected, actual);
            }
        }

        @Nested
        class RemoveConstant {

            @Test
            void constantDoesNotExistNoException() throws PMException {
                assertDoesNotThrow(() -> policyStore.userDefinedPML().deleteConstant("const1"));
            }

            @Test
            void success() throws PMException {
                policyStore.userDefinedPML().createConstant("const1", new Value("test"));
                assertTrue(policyStore.userDefinedPML().getConstants().containsKey("const1"));
                policyStore.userDefinedPML().deleteConstant("const1");
                assertFalse(policyStore.userDefinedPML().getConstants().containsKey("const1"));
            }
        }

        @Nested
        class GetConstants {

            @Test
            void success() throws PMException {
                Value const1 = new Value("test1");
                Value const2 = new Value("test2");

                policyStore.userDefinedPML().createConstant("const1", const1);
                policyStore.userDefinedPML().createConstant("const2", const2);

                Map<String, Value> constants = policyStore.userDefinedPML().getConstants();
                assertTrue(constants.containsKey("const1"));
                Value actual = constants.get("const1");
                assertEquals(const1, actual);

                assertTrue(constants.containsKey("const2"));
                actual = constants.get("const2");
                assertEquals(const2, actual);
            }
        }

    }
}