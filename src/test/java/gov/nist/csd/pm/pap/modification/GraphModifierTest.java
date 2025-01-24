package gov.nist.csd.pm.pap.modification;

import gov.nist.csd.pm.common.exception.*;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.common.graph.relationship.InvalidAssignmentException;
import gov.nist.csd.pm.common.graph.relationship.InvalidAssociationException;
import gov.nist.csd.pm.common.obligation.EventPattern;
import gov.nist.csd.pm.common.obligation.Response;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.PAPTestInitializer;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.InSubjectPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static gov.nist.csd.pm.common.graph.node.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.common.graph.node.Properties.toProperties;
import static gov.nist.csd.pm.pap.AdminAccessRights.*;
import static gov.nist.csd.pm.pap.AdminAccessRights.ALL_ADMIN_ACCESS_RIGHTS;
import static org.junit.jupiter.api.Assertions.*;

public abstract class GraphModifierTest extends PAPTestInitializer {

    @Nested
    class CreatePolicyClassTest {
        @Test
        void testNodeNameExistsException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            assertDoesNotThrow(() -> pap.modify().graph().createPolicyClass("pc2"));
            assertThrows(NodeNameExistsException.class, () -> pap.modify().graph().createPolicyClass("pc1"));
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            assertTrue(pap.query().graph().nodeExists("pc1"));
        }

        @Test
        void testTx() throws PMException {
            pap.runTx(tx -> {
                PolicyModification modify = pap.modify();
                modify.graph().createPolicyClass("pc1");
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                PolicyModification modify = pap.modify();
                modify.graph().createPolicyClass("pc2");
                modify.graph().createPolicyClass("pc3");
                throw new PMException("");
            }));
            assertTrue(pap.query().graph().nodeExists("pc1"));
            assertFalse(pap.query().graph().nodeExists("pc2"));
            assertFalse(pap.query().graph().nodeExists("pc3"));
        }
    }

    @Nested
    class CreateObjectAttribute {

        @Test
        void testNodeNameExistsException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            assertThrows(NodeNameExistsException.class,
                    () -> pap.modify().graph().createObjectAttribute("oa1", List.of("pc1")));
        }

        @Test
        void testNodeDoesNotExistException() throws PMException {
            assertThrows(
                    NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createObjectAttribute("oa1", List.of("pc1")));

            pap.modify().graph().createPolicyClass("pc1");

            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createObjectAttribute("oa1", List.of("pc1", "pc2")));
        }

        @Test
        void testInvalidAssignmentException()
                throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));

            assertThrows(
                    InvalidAssignmentException.class,
                    () -> pap.modify().graph().createObjectAttribute("oa1", List.of("ua1")));
        }

        @Test
        void testAssignmentCausesLoopException()
                throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", List.of("oa1"));

            assertThrows(
                    AssignmentCausesLoopException.class,
                    () -> pap.modify().graph().createObjectAttribute("oa3", List.of("oa3")));
            assertThrows(AssignmentCausesLoopException.class,
                    () -> pap.modify().graph().createObjectAttribute("oa3", List.of("oa2", "oa3")));
        }

        @Test
        void Success() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", List.of("oa1"));
            pap.modify().graph().setNodeProperties("oa2", toProperties("k", "v"));

            assertTrue(pap.query().graph().nodeExists("oa1"));
            assertTrue(pap.query().graph().nodeExists("oa2"));
            assertEquals("v", pap.query().graph().getNodeByName("oa2").getProperties().get("k"));

            assertTrue(pap.query().graph().getAdjacentAscendants("pc1").contains("oa1"));
            assertTrue(pap.query().graph().getAdjacentAscendants("oa1").contains("oa2"));

            assertTrue(pap.query().graph().getAdjacentDescendants("oa1").contains("pc1"));
            assertTrue(pap.query().graph().getAdjacentDescendants("oa2").contains("oa1"));
        }

        @Test
        void testNoAssignments() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            assertThrows(DisconnectedNodeException.class, () -> pap.modify().graph().createObjectAttribute("oa1", List.of()));
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.runTx(tx -> {
                pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                PolicyModification modify = pap.modify();
                pap.modify().graph().createObjectAttribute("oa2", List.of("pc1"));
                pap.modify().graph().createObjectAttribute("oa3", List.of("pc1"));
                throw new PMException("");
            }));
            assertTrue(pap.query().graph().nodeExists("oa1"));
            assertFalse(pap.query().graph().nodeExists("oa2"));
            assertFalse(pap.query().graph().nodeExists("oa3"));
        }
    }

    @Nested
    class CreateUserAttributeTest {

        @Test
        void testNodeNameExistsException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            assertThrows(NodeNameExistsException.class,
                    () -> pap.modify().graph().createObjectAttribute("ua1", List.of("pc1")));
        }

        @Test
        void testNodeDoesNotExistException() throws PMException {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createUserAttribute("ua1", List.of("pc1")));

            pap.modify().graph().createPolicyClass("pc1");

            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createUserAttribute("ua1", List.of("pc1", "pc2")));
        }

        @Test
        void testInvalidAssignmentException()
                throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));

            assertThrows(InvalidAssignmentException.class,
                    () -> pap.modify().graph().createUserAttribute("ua1", List.of("oa1")));
        }

        @Test
        void testAssignmentCausesLoopException()
                throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua2", List.of("ua1"));

            assertThrows(AssignmentCausesLoopException.class,
                    () -> pap.modify().graph().createUserAttribute("ua3", List.of("ua3")));
            assertThrows(AssignmentCausesLoopException.class,
                    () -> pap.modify().graph().createUserAttribute("ua3", List.of("ua2", "ua3")));
        }

        @Test
        void Success() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua2", List.of("ua1"));
            pap.modify().graph().setNodeProperties("ua2", toProperties("k", "v"));

            assertTrue(pap.query().graph().nodeExists("ua1"));
            assertTrue(pap.query().graph().nodeExists("ua2"));
            assertEquals("v", pap.query().graph().getNodeByName("ua2").getProperties().get("k"));

            assertTrue(pap.query().graph().getAdjacentAscendants("pc1").contains("ua1"));
            assertTrue(pap.query().graph().getAdjacentDescendants("ua1").contains("pc1"));

            assertTrue(pap.query().graph().getAdjacentAscendants("ua1").contains("ua2"));
            assertTrue(pap.query().graph().getAdjacentDescendants("ua2").contains("ua1"));
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.runTx(tx -> {
                pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));
                pap.modify().graph().createUserAttribute("ua3", List.of("pc1"));
                throw new PMException("");
            }));
            assertTrue(pap.query().graph().nodeExists("ua1"));
            assertFalse(pap.query().graph().nodeExists("ua2"));
            assertFalse(pap.query().graph().nodeExists("ua3"));
        }
    }

    @Nested
    class CreateObjectTest {

        @Test
        void testNodeNameExistsException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObject("o1", List.of("oa1"));
            assertThrows(NodeNameExistsException.class,
                    () -> pap.modify().graph().createObject("o1", List.of("oa1")));
        }

        @Test
        void testNodeDoesNotExistException() throws PMException {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createObject("o1", List.of("oa1")));

            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));

            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createObjectAttribute("o1", List.of("oa1", "oa2")));
        }

        @Test
        void testInvalidAssignmentException()
                throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));

            assertThrows(InvalidAssignmentException.class,
                    () -> pap.modify().graph().createObjectAttribute("o1", List.of("ua1")));
        }

        @Test
        void testAssignmentCausesLoopException()
                throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));

            assertThrows(AssignmentCausesLoopException.class,
                    () -> pap.modify().graph().createObject("o1", List.of("o1")));
            assertThrows(AssignmentCausesLoopException.class,
                    () -> pap.modify().graph().createObject("o1", List.of("oa1", "o1")));
        }

        @Test
        void Success() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));

            pap.modify().graph().createObject("o1", List.of("oa1"));
            pap.modify().graph().setNodeProperties("o1", toProperties("k", "v"));

            assertTrue(pap.query().graph().nodeExists("o1"));
            assertEquals("v", pap.query().graph().getNodeByName("o1").getProperties().get("k"));

            assertTrue(pap.query().graph().getAdjacentAscendants("oa1").contains("o1"));
            assertTrue(pap.query().graph().getAdjacentDescendants("o1").contains("oa1"));
            assertTrue(pap.query().graph().getAdjacentAscendants("oa1").contains("o1"));
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.runTx(tx -> {
                pap.modify().graph().createObject("o1", List.of("oa1"));
            });
            PMException e = assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().createObject("o2", List.of("oa1"));
                pap.modify().graph().createObject("o3", List.of("oa1"));
                throw new PMException("test");
            }));
            assertEquals("test", e.getMessage());
            assertTrue(pap.query().graph().nodeExists("o1"));
            assertFalse(pap.query().graph().nodeExists("o2"));
            assertFalse(pap.query().graph().nodeExists("o3"));
        }
    }

    @Nested
    class CreateUserTest {

        @Test
        void testNodeNameExistsException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUser("u1", List.of("ua1"));
            assertThrows(NodeNameExistsException.class,
                    () -> pap.modify().graph().createUser("u1", List.of("ua1")));
        }

        @Test
        void testNodeDoesNotExistException() throws PMException {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createUser("u1", List.of("ua1")));

            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));

            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createUser("u1", List.of("ua1", "ua2")));
        }

        @Test
        void testInvalidAssignmentException()
                throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));

            assertThrows(InvalidAssignmentException.class,
                    () -> pap.modify().graph().createUser("u1", List.of("oa1")));
        }

        @Test
        void testAssignmentCausesLoopException()
                throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));

            assertThrows(AssignmentCausesLoopException.class,
                    () -> pap.modify().graph().createUser("u1", List.of("u1")));
            assertThrows(AssignmentCausesLoopException.class,
                    () -> pap.modify().graph().createUser("u1", List.of("ua1", "u1")));
        }

        @Test
        void Success() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));

            pap.modify().graph().createUser("u1", List.of("ua1"));
            pap.modify().graph().setNodeProperties("u1", toProperties("k", "v"));

            assertTrue(pap.query().graph().nodeExists("u1"));
            assertEquals("v", pap.query().graph().getNodeByName("u1").getProperties().get("k"));

            assertTrue(pap.query().graph().getAdjacentAscendants("ua1").contains("u1"));
            assertTrue(pap.query().graph().getAdjacentDescendants("u1").contains("ua1"));
            assertTrue(pap.query().graph().getAdjacentAscendants("ua1").contains("u1"));
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.runTx(tx -> {
                pap.modify().graph().createUser("u1", List.of("ua1"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().createUser("u2", List.of("ua1"));
                pap.modify().graph().createUser("u3", List.of("ua1"));
                throw new PMException("");
            }));
            assertTrue(pap.query().graph().nodeExists("u1"));
            assertFalse(pap.query().graph().nodeExists("u2"));
            assertFalse(pap.query().graph().nodeExists("u3"));
        }
    }

    @Nested
    class SetNodePropertiesTest {

        @Test
        void testNodeDoesNotExistException() {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().setNodeProperties("oa1", NO_PROPERTIES));
        }

        @Test
        void testSuccessEmptyProperties() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().setNodeProperties("pc1", NO_PROPERTIES);

            assertTrue(pap.query().graph().getNodeByName("pc1").getProperties().isEmpty());
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().setNodeProperties("pc1", toProperties("k", "v"));

            assertEquals("v", pap.query().graph().getNodeByName("pc1").getProperties().get("k"));
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.runTx(tx -> {
                pap.modify().graph().createUser("u1", List.of("ua1"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().createUser("u2", List.of("ua1"));
                pap.modify().graph().createUser("u3", List.of("ua1"));
                throw new PMException("");
            }));
            assertTrue(pap.query().graph().nodeExists("u1"));
            assertFalse(pap.query().graph().nodeExists("u2"));
            assertFalse(pap.query().graph().nodeExists("u3"));
        }
    }

    @Nested
    class DeleteNodeTest {

        @Test
        void testNodeDoesNotExistDoesNotThrowException() {
            assertDoesNotThrow(() -> pap.modify().graph().deleteNode("pc1"));
        }

        @Test
        void testNodeHasAscendantsException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));

            assertThrows(
                    NodeHasAscendantsException.class,
                    () -> pap.modify().graph().deleteNode("pc1"));
        }

        @Test
        void DeleteNodeWithProhibitionsAndObligations() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));
            pap.modify().graph().createUser("u1", List.of("ua2"));
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().prohibitions().createProhibition("pro1", ProhibitionSubject.userAttribute("ua1"), ,
                    new AccessRightSet(), true,
                    Collections.singleton(new ContainerCondition("oa1", true)));

            assertThrows(NodeReferencedInProhibitionException.class,
                    () -> pap.modify().graph().deleteNode("ua1"));
            assertThrows(NodeReferencedInProhibitionException.class,
                    () -> pap.modify().graph().deleteNode("oa1"));

            pap.modify().prohibitions().deleteProhibition("pro1");
            pap.modify().obligations().createObligation("u1", "oblLabel",
                    List.of(new Rule(
                            "rule1",
                            new EventPattern(
                                    new SubjectPattern(new InSubjectPattern("ua1")),
                                    new OperationPattern("event1")
                            ),
                            new Response("evtCtx", List.of())
                    ),
                    new Rule(
                            "rule1",
                            new EventPattern(
                                    new SubjectPattern(new InSubjectPattern("ua1")),
                                    new OperationPattern("event1")
                            ),
                            new Response("evtCtx", List.of())
                    ))
            );

            assertThrows(NodeReferencedInObligationException.class,
                    () -> pap.modify().graph().deleteNode("ua1"));
        }

        @Test
        void testSuccessPolicyClass() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().deleteNode("pc1");
            assertFalse(pap.query().graph().nodeExists("pc1"));
        }

        @Test
        void testSuccessObjectAttribute() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));

            pap.modify().graph().deleteNode("oa1");

            assertFalse(pap.query().graph().nodeExists("oa1"));
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().associate("ua2", "oa1", new AccessRightSet("*"));

            pap.runTx(tx -> {
                pap.modify().graph().deleteNode("ua1");
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().deleteNode("ua2");
                throw new PMException("");
            }));
            assertTrue(pap.query().graph().nodeExists("ua2"));
            assertTrue(pap.query().graph().isAscendant("ua2", "pc1"));
            assertTrue(pap.query().graph().getAssociationsWithSource("ua2").contains(new Association("ua2", "oa1", new AccessRightSet("*"))));
            assertFalse(pap.query().graph().nodeExists("ua1"));
        }
    }

    @Nested
    class AssignTest {

        @Test
        void testAscNodeDoesNotExistException() {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().assign("oa1", List.of("pc1")));
        }

        @Test
        void testDescNodeDoesNotExistException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().assign("oa1", List.of("oa2")));
        }

        @Test
        void testAssignmentExistsDoesNothing() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            assertDoesNotThrow(() -> pap.modify().graph().assign("oa1", List.of("pc1")));
        }

        @Test
        void testInvalidAssignmentException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));

            assertThrows(InvalidAssignmentException.class,
                    () -> pap.modify().graph().assign("ua1", List.of("oa1")));
        }

        @Test
        void testAssignmentCausesLoopException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", List.of("oa1"));
            pap.modify().graph().createObjectAttribute("oa3", List.of("oa2"));

            assertThrows(AssignmentCausesLoopException.class, () ->
                    pap.modify().graph().assign("oa1", List.of("oa2")));
            assertThrows(AssignmentCausesLoopException.class, () ->
                    pap.modify().graph().assign("oa1", List.of("oa1")));
            assertThrows(AssignmentCausesLoopException.class, () ->
                    pap.modify().graph().assign("oa1", List.of("oa3")));
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", List.of("pc1"));
            pap.modify().graph().assign("oa2", List.of("oa1"));
            assertTrue(pap.query().graph().getAdjacentDescendants("oa2").contains("oa1"));
            assertTrue(pap.query().graph().getAdjacentAscendants("oa1").contains("oa2"));
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua3", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua4", List.of("pc1"));

            pap.runTx(tx -> {
                pap.modify().graph().assign("ua4", Collections.singleton("ua1"));
                pap.modify().graph().assign("ua4", Collections.singleton("ua2"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().assign("ua4", Collections.singleton("ua3"));
                throw new PMException("");
            }));

            assertTrue(pap.query().graph().isAscendant("ua4", "ua1"));
            assertTrue(pap.query().graph().isAscendant("ua4", "ua2"));
            assertFalse(pap.query().graph().isAscendant("ua4", "ua3"));
        }

        @Test
        void testOneDescendantIsAlreadyAssigned() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));

            pap.modify().graph().assign("ua2", List.of("pc1", "ua1"));

            assertTrue(pap.query().graph().isAscendant("ua2", "pc1"));
            assertTrue(pap.query().graph().isAscendant("ua2", "ua1"));
        }
    }

    @Nested
    class DeassignTest {

        @Test
        void testAscNodeDoesNotExistException() {
            assertThrows(NodeDoesNotExistException.class, () ->
                    pap.modify().graph().deassign("oa1", List.of("pc1")));
        }

        @Test
        void testDescNodeDoesNotExistException() throws PMException{
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));

            assertThrows(NodeDoesNotExistException.class, () ->
                    pap.modify().graph().deassign("oa1", List.of("oa2")));
        }

        @Test
        void AssignmentDoesNotExistDoesNothing() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", List.of("pc1"));
            pap.modify().graph().deassign("oa1", List.of("oa2"));
        }

        @Test
        void testDisconnectedNode() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));

            assertThrows(DisconnectedNodeException.class,
                    () -> pap.modify().graph().deassign("oa1", List.of("pc1")));
        }

        @Test
        void Success() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createPolicyClass("pc2");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1", "pc2"));
            pap.modify().graph().deassign("oa1", List.of("pc1"));
            assertTrue(pap.query().graph().getAdjacentDescendants("oa1").contains("pc2"));
            assertFalse(pap.query().graph().getAdjacentDescendants("oa1").contains("pc1"));
            assertFalse(pap.query().graph().getAdjacentAscendants("pc1").contains("oa1"));
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua3", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua4", List.of("ua1", "ua2", "ua3"));

            pap.runTx(tx -> {
                pap.modify().graph().deassign("ua4", Collections.singleton("ua1"));
                pap.modify().graph().deassign("ua4", Collections.singleton("ua2"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().deassign("ua4", Collections.singleton("ua3"));
                throw new PMException("");
            }));

            assertFalse(pap.query().graph().isAscendant("ua4", "ua1"));
            assertFalse(pap.query().graph().isAscendant("ua4", "ua2"));
            assertTrue(pap.query().graph().isAscendant("ua4", "ua3"));
        }

        @Test
        void testOneDescendantIsAlreadyDeassigned() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua3", List.of("ua1", "ua2"));

            pap.modify().graph().deassign("ua3", List.of("pc1", "ua1"));

            assertFalse(pap.query().graph().getAdjacentDescendants("ua3").contains("pc1"));
            assertFalse(pap.query().graph().getAdjacentDescendants("ua3").contains("ua1"));
        }
    }

    @Nested
    class AssociateTest {

        @Test
        void testUANodeDoesNotExistException() {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().associate("ua1", "oa1", new AccessRightSet()));
        }

        @Test
        void testTargetNodeDoesNotExistException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));

            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().associate("ua1", "oa1", new AccessRightSet()));
        }

        @Test
        void testAssignmentExistsDoesNotThrowException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua2", List.of("ua1"));
            assertDoesNotThrow(() -> pap.modify().graph().associate("ua2", "ua1", new AccessRightSet()));
        }

        @Test
        void testUnknownAccessRightException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            assertThrows(UnknownAccessRightException.class,
                    () -> pap.modify().graph().associate("ua1", "oa1", new AccessRightSet("read")));
            pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
            assertThrows(UnknownAccessRightException.class,
                    () -> pap.modify().graph().associate("ua1", "oa1", new AccessRightSet("write")));
            assertDoesNotThrow(() -> pap.modify().graph().associate("ua1", "oa1", new AccessRightSet("read")));
            assertDoesNotThrow(() -> pap.modify().graph().associate("ua1", "oa1", new AccessRightSet(ALL_ACCESS_RIGHTS)));
            assertDoesNotThrow(() -> pap.modify().graph().associate("ua1", "oa1", new AccessRightSet(ALL_RESOURCE_ACCESS_RIGHTS)));
            assertDoesNotThrow(() -> pap.modify().graph().associate("ua1", "oa1", new AccessRightSet(ALL_ADMIN_ACCESS_RIGHTS)));
        }

        @Test
        void testInvalidAssociationException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua2", List.of("ua1"));
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", List.of("pc1"));

            assertThrows(
                    InvalidAssociationException.class,
                    () -> pap.modify().graph().associate("ua2", "pc1", new AccessRightSet()));
            assertThrows(InvalidAssociationException.class,
                    () -> pap.modify().graph().associate("oa1", "oa2", new AccessRightSet()));
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));

            pap.modify().operations().setResourceOperations(new AccessRightSet("read", "write"));
            pap.modify().graph().associate("ua1", "oa1", new AccessRightSet("read"));

            assertEquals(
                    new Association("ua1", "oa1", new AccessRightSet("read")),
                    pap.query().graph().getAssociationsWithSource("ua1").iterator().next()
            );
            assertEquals(
                    new Association("ua1", "oa1", new AccessRightSet("read")),
                    pap.query().graph().getAssociationsWithTarget("oa1").iterator().next()
            );
        }

        @Test
        void testOverwriteSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));

            pap.modify().operations().setResourceOperations(new AccessRightSet("read", "write"));
            pap.modify().graph().associate("ua1", "oa1", new AccessRightSet("read"));

            Collection<Association> assocs = pap.query().graph().getAssociationsWithSource("ua1");
            Association assoc = assocs.iterator().next();
            assertEquals("ua1", assoc.getSource());
            assertEquals("oa1", assoc.getTarget());
            assertEquals(new AccessRightSet("read"), assoc.getAccessRightSet());

            pap.modify().graph().associate("ua1", "oa1", new AccessRightSet("read", "write"));

            assocs = pap.query().graph().getAssociationsWithSource("ua1");
            assoc = assocs.iterator().next();
            assertEquals("ua1", assoc.getSource());
            assertEquals("oa1", assoc.getTarget());
            assertEquals(new AccessRightSet("read", "write"), assoc.getAccessRightSet());
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua3", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua4", List.of("ua1", "ua2", "ua3"));

            pap.runTx(tx -> {
                pap.modify().graph().associate("ua4", "ua1", new AccessRightSet("*"));
                pap.modify().graph().associate("ua4", "ua2", new AccessRightSet("*"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().associate("ua4", "ua3", new AccessRightSet("*"));
                throw new PMException("");
            }));

            assertTrue(pap.query().graph().getAssociationsWithSource("ua4").containsAll(List.of(
                    new Association("ua4", "ua1", new AccessRightSet("*")),
                    new Association("ua4", "ua2", new AccessRightSet("*"))
            )));
            assertFalse(pap.query().graph().getAssociationsWithSource("ua4").contains(
                    new Association("ua4", "ua3", new AccessRightSet("*"))
            ));
        }
    }

    @Nested
    class DissociateTest {

        @Test
        void testUANodeDoesNotExistException() {
            assertThrows(NodeDoesNotExistException.class, () -> pap.modify().graph().dissociate("ua1", "oa1"));
        }

        @Test
        void testTargetNodeDoesNotExistException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));

            assertThrows(NodeDoesNotExistException.class, () -> pap.modify().graph().dissociate("ua1", "oa2"));
        }

        @Test
        void testAssociationDoesNotExistDoesNotThrowException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));

            assertDoesNotThrow(() -> pap.modify().graph().dissociate("ua1", "oa1"));
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().associate("ua1", "oa1", new AccessRightSet());

            pap.modify().graph().dissociate("ua1", "oa1");

            assertTrue(pap.query().graph().getAssociationsWithSource("ua1").isEmpty());
            assertTrue(pap.query().graph().getAssociationsWithTarget("oa1").isEmpty());
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua3", List.of("pc1"));
            pap.modify().graph().createUserAttribute("ua4", List.of("ua1", "ua2", "ua3"));

            pap.modify().graph().associate("ua4", "ua1", new AccessRightSet("*"));
            pap.modify().graph().associate("ua4", "ua2", new AccessRightSet("*"));
            pap.modify().graph().associate("ua4", "ua3", new AccessRightSet("*"));

            pap.runTx(tx -> {
                pap.modify().graph().dissociate("ua4", "ua1");
                pap.modify().graph().dissociate("ua4", "ua2");
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().dissociate("ua4", "ua3");
                throw new PMException("");
            }));

            assertFalse(pap.query().graph().getAssociationsWithSource("ua4").containsAll(List.of(
                    new Association("ua4", "ua1", new AccessRightSet("*")),
                    new Association("ua4", "ua2", new AccessRightSet("*"))
            )));
            assertTrue(pap.query().graph().getAssociationsWithSource("ua4").contains(
                    new Association("ua4", "ua3", new AccessRightSet("*"))
            ));
        }
    }
}