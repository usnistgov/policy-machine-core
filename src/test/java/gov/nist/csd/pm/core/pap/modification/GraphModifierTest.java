package gov.nist.csd.pm.core.pap.modification;

import gov.nist.csd.pm.core.common.exception.*;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.graph.relationship.Association;
import gov.nist.csd.pm.core.common.graph.relationship.InvalidAssignmentException;
import gov.nist.csd.pm.core.common.graph.relationship.InvalidAssociationException;
import gov.nist.csd.pm.core.pap.obligation.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.PMLObligationResponse;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.pap.PAPTestInitializer;
import gov.nist.csd.pm.core.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.InSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.SubjectPattern;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static gov.nist.csd.pm.core.common.graph.node.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.core.common.graph.node.Properties.toProperties;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.*;
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
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            assertThrows(NodeNameExistsException.class,
                    () -> pap.modify().graph().createObjectAttribute("oa1", ids("pc1")));
        }

        @Test
        void testNodeDoesNotExistException() throws PMException {
            assertThrows(
                    NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createObjectAttribute("oa1", ids("pc1")));

            pap.modify().graph().createPolicyClass("pc1");

            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createObjectAttribute("oa1", ids("pc1", "pc2")));
        }

        @Test
        void testInvalidAssignmentException()
                throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));

            assertThrows(
                    InvalidAssignmentException.class,
                    () -> pap.modify().graph().createObjectAttribute("oa1", ids("ua1")));
        }

        @Test
        void Success() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", ids("oa1"));
            pap.modify().graph().setNodeProperties(id("oa2"), toProperties("k", "v"));

            assertTrue(pap.query().graph().nodeExists("oa1"));
            assertTrue(pap.query().graph().nodeExists("oa2"));
            assertEquals("v", pap.query().graph().getNodeByName("oa2").getProperties().get("k"));

            assertTrue(pap.query().graph().getAdjacentAscendants(id("pc1")).contains(id("oa1")));
            assertIdOfNameInLongArray(pap.query().graph().getAdjacentAscendants(id("oa1")), "oa2");
            assertIdOfNameInLongArray(pap.query().graph().getAdjacentDescendants(id("oa1")), "pc1");
            assertIdOfNameInLongArray(pap.query().graph().getAdjacentDescendants(id("oa2")), "oa1");
        }

        @Test
        void testNoAssignments() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            assertThrows(DisconnectedNodeException.class, () -> pap.modify().graph().createObjectAttribute("oa1", ids()));
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.runTx(tx -> {
                pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                PolicyModification modify = pap.modify();
                pap.modify().graph().createObjectAttribute("oa2", ids("pc1"));
                pap.modify().graph().createObjectAttribute("oa3", ids("pc1"));
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
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            assertThrows(NodeNameExistsException.class,
                    () -> pap.modify().graph().createObjectAttribute("ua1", ids("pc1")));
        }

        @Test
        void testNodeDoesNotExistException() throws PMException {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createUserAttribute("ua1", ids("pc1")));

            pap.modify().graph().createPolicyClass("pc1");

            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createUserAttribute("ua1", ids("pc1", "pc2")));
        }

        @Test
        void testInvalidAssignmentException()
                throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));

            assertThrows(InvalidAssignmentException.class,
                    () -> pap.modify().graph().createUserAttribute("ua1", ids("oa1")));
        }

        @Test
        void Success() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua2", ids("ua1"));
            pap.modify().graph().setNodeProperties(id("ua2"), toProperties("k", "v"));

            assertTrue(pap.query().graph().nodeExists("ua1"));
            assertTrue(pap.query().graph().nodeExists("ua2"));
            assertEquals("v", pap.query().graph().getNodeByName("ua2").getProperties().get("k"));

            assertIdOfNameInLongArray(pap.query().graph().getAdjacentAscendants(id("pc1")), "ua1");
            assertIdOfNameInLongArray(pap.query().graph().getAdjacentDescendants(id("ua1")), "pc1");

            assertIdOfNameInLongArray(pap.query().graph().getAdjacentAscendants(id("ua1")), "ua2");
            assertIdOfNameInLongArray(pap.query().graph().getAdjacentDescendants(id("ua2")), "ua1");
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.runTx(tx -> {
                pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().createUserAttribute("ua2", ids("pc1"));
                pap.modify().graph().createUserAttribute("ua3", ids("pc1"));
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
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.modify().graph().createObject("o1", ids("oa1"));
            assertThrows(NodeNameExistsException.class,
                    () -> pap.modify().graph().createObject("o1", ids("oa1")));
        }

        @Test
        void testNodeDoesNotExistException() throws PMException {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createObject("o1", ids("oa1")));

            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));

            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createObjectAttribute("o1", ids("oa1", "oa2")));
        }

        @Test
        void testInvalidAssignmentException()
                throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));

            assertThrows(InvalidAssignmentException.class,
                    () -> pap.modify().graph().createObjectAttribute("o1", ids("ua1")));
        }

        @Test
        void Success() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));

            pap.modify().graph().createObject("o1", ids("oa1"));
            pap.modify().graph().setNodeProperties(id("o1"), toProperties("k", "v"));

            assertTrue(pap.query().graph().nodeExists("o1"));
            assertEquals("v", pap.query().graph().getNodeByName("o1").getProperties().get("k"));

            assertIdOfNameInLongArray(pap.query().graph().getAdjacentAscendants(id("oa1")), "o1");
            assertIdOfNameInLongArray(pap.query().graph().getAdjacentDescendants(id("o1")), "oa1");
            assertIdOfNameInLongArray(pap.query().graph().getAdjacentAscendants(id("oa1")), "o1");
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.runTx(tx -> {
                pap.modify().graph().createObject("o1", ids("oa1"));
            });
            PMException e = assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().createObject("o2", ids("oa1"));
                pap.modify().graph().createObject("o3", ids("oa1"));
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
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUser("u1", ids("ua1"));
            assertThrows(NodeNameExistsException.class,
                    () -> pap.modify().graph().createUser("u1", ids("ua1")));
        }

        @Test
        void testNodeDoesNotExistException() throws PMException {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createUser("u1", ids("ua1")));

            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));

            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().createUser("u1", ids("ua1", "ua2")));
        }

        @Test
        void testInvalidAssignmentException()
                throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));

            assertThrows(InvalidAssignmentException.class,
                    () -> pap.modify().graph().createUser("u1", ids("oa1")));
        }

        @Test
        void Success() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));

            pap.modify().graph().createUser("u1", ids("ua1"));
            pap.modify().graph().setNodeProperties(id("u1"), toProperties("k", "v"));

            assertTrue(pap.query().graph().nodeExists("u1"));
            assertEquals("v", pap.query().graph().getNodeByName("u1").getProperties().get("k"));

            assertIdOfNameInLongArray(pap.query().graph().getAdjacentAscendants(id("ua1")), "u1");
            assertIdOfNameInLongArray(pap.query().graph().getAdjacentDescendants(id("u1")), "ua1");
            assertIdOfNameInLongArray(pap.query().graph().getAdjacentAscendants(id("ua1")), "u1");
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.runTx(tx -> {
                pap.modify().graph().createUser("u1", ids("ua1"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().createUser("u2", ids("ua1"));
                pap.modify().graph().createUser("u3", ids("ua1"));
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
                    () -> pap.modify().graph().setNodeProperties(id("oa1"), NO_PROPERTIES));
        }

        @Test
        void testSuccessEmptyProperties() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().setNodeProperties(id("pc1"), NO_PROPERTIES);

            assertTrue(pap.query().graph().getNodeByName("pc1").getProperties().isEmpty());
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().setNodeProperties(id("pc1"), toProperties("k", "v"));

            assertEquals("v", pap.query().graph().getNodeByName("pc1").getProperties().get("k"));
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.runTx(tx -> {
                pap.modify().graph().createUser("u1", ids("ua1"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().createUser("u2", ids("ua1"));
                pap.modify().graph().createUser("u3", ids("ua1"));
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
            assertDoesNotThrow(() -> pap.modify().graph().deleteNode(0));
        }

        @Test
        void testNodeHasAscendantsException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));

            assertThrows(
                    NodeHasAscendantsException.class,
                    () -> pap.modify().graph().deleteNode(id("pc1")));
        }

        @Test
        void DeleteNodeWithProhibitionsAndObligations() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua2", ids("pc1"));
            pap.modify().graph().createUser("u1", ids("ua2"));
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.modify().prohibitions().createProhibition(
                    "pro1", 
                    new ProhibitionSubject(id("ua1")),
                    new AccessRightSet(), 
                    true,
                    List.of(new ContainerCondition(id("oa1"), true))
            );

            assertThrows(NodeReferencedInProhibitionException.class,
                    () -> pap.modify().graph().deleteNode(id("ua1")));
            assertThrows(NodeReferencedInProhibitionException.class,
                    () -> pap.modify().graph().deleteNode(id("oa1")));

            pap.modify().prohibitions().deleteProhibition("pro1");
            pap.modify().obligations().createObligation(id("u1"), "oblLabel",
                    List.of(new Rule(
                            "rule1",
                            new EventPattern(
                                    new SubjectPattern(new InSubjectPatternExpression("ua1")),
                                    new OperationPattern("event1")
                            ),
                            new PMLObligationResponse("evtCtx", List.of())
                    ),
                    new Rule(
                            "rule1",
                            new EventPattern(
                                    new SubjectPattern(new InSubjectPatternExpression("ua1")),
                                    new OperationPattern("event1")
                            ),
                            new PMLObligationResponse("evtCtx", List.of())
                    ))
            );

            assertThrows(NodeReferencedInObligationException.class,
                    () -> pap.modify().graph().deleteNode(id("ua1")));
        }

        @Test
        void testSuccessPolicyClass() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().deleteNode(id("pc1"));
            assertFalse(pap.query().graph().nodeExists("pc1"));
        }

        @Test
        void testSuccessObjectAttribute() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));

            pap.modify().graph().deleteNode(id("oa1"));

            assertFalse(pap.query().graph().nodeExists("oa1"));
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua2", ids("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.modify().graph().associate(id("ua2"), id("oa1"), new AccessRightSet("*"));

            pap.runTx(tx -> {
                pap.modify().graph().deleteNode(id("ua1"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().deleteNode(id("ua2"));
                throw new PMException("");
            }));
            assertTrue(pap.query().graph().nodeExists("ua2"));
            assertTrue(pap.query().graph().isAscendant(id("ua2"), id("pc1")));
            assertTrue(new HashSet<>(pap.query().graph().getAssociationsWithSource(id("ua2")))
                    .contains(new Association(id("ua2"), id("oa1"), new AccessRightSet("*"))));
            assertFalse(pap.query().graph().nodeExists("ua1"));
        }
    }

    @Nested
    class AssignTest {

        @Test
        void testAscNodeDoesNotExistException() {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().assign(id("oa1"), ids("pc1")));
        }

        @Test
        void testDescNodeDoesNotExistException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().assign(id("oa1"), ids("oa2")));
        }

        @Test
        void testAssignmentExistsDoesNothing() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            assertDoesNotThrow(() -> pap.modify().graph().assign(id("oa1"), ids("pc1")));
        }

        @Test
        void testInvalidAssignmentException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));

            assertThrows(InvalidAssignmentException.class,
                    () -> pap.modify().graph().assign(id("ua1"), ids("oa1")));
        }

        @Test
        void testAssignmentCausesLoopException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", ids("oa1"));
            pap.modify().graph().createObjectAttribute("oa3", ids("oa2"));

            assertThrows(AssignmentCausesLoopException.class, () ->
                    pap.modify().graph().assign(id("oa1"), ids("oa2")));
            assertThrows(AssignmentCausesLoopException.class, () ->
                    pap.modify().graph().assign(id("oa1"), ids("oa1")));
            assertThrows(AssignmentCausesLoopException.class, () ->
                    pap.modify().graph().assign(id("oa1"), ids("oa3")));
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", ids("pc1"));
            pap.modify().graph().assign(id("oa2"), ids("oa1"));
            assertIdOfNameInLongArray(pap.query().graph().getAdjacentDescendants(id("oa2")), "oa1");
            assertIdOfNameInLongArray(pap.query().graph().getAdjacentAscendants(id("oa1")), "oa2");
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua2", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua3", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua4", ids("pc1"));

            pap.runTx(tx -> {
                pap.modify().graph().assign(id("ua4"), ids("ua1"));
                pap.modify().graph().assign(id("ua4"), ids("ua2"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().assign(id("ua4"), ids("ua3"));
                throw new PMException("");
            }));

            assertTrue(pap.query().graph().isAscendant(id("ua4"), id("ua1")));
            assertTrue(pap.query().graph().isAscendant(id("ua4"), id("ua2")));
            assertFalse(pap.query().graph().isAscendant(id("ua4"), id("ua3")));
        }

        @Test
        void testOneDescendantIsAlreadyAssigned() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua2", ids("pc1"));

            pap.modify().graph().assign(id("ua2"), ids("pc1", "ua1"));

            assertTrue(pap.query().graph().isAscendant(id("ua2"), id("pc1")));
            assertTrue(pap.query().graph().isAscendant(id("ua2"), id("ua1")));
        }
    }

    @Nested
    class DeassignTest {

        @Test
        void testAscNodeDoesNotExistException() {
            assertThrows(NodeDoesNotExistException.class, () ->
                    pap.modify().graph().deassign(id("oa1"), ids("pc1")));
        }

        @Test
        void testDescNodeDoesNotExistException() throws PMException{
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));

            assertThrows(NodeDoesNotExistException.class, () ->
                    pap.modify().graph().deassign(id("oa1"), ids("oa2")));
        }

        @Test
        void AssignmentDoesNotExistDoesNothing() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", ids("pc1"));
            pap.modify().graph().deassign(id("oa1"), ids("oa2"));
        }

        @Test
        void testDisconnectedNode() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));

            assertThrows(DisconnectedNodeException.class,
                    () -> pap.modify().graph().deassign(id("oa1"), ids("pc1")));
        }

        @Test
        void Success() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createPolicyClass("pc2");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1", "pc2"));
            pap.modify().graph().deassign(id("oa1"), ids("pc1"));
            assertIdOfNameInLongArray(pap.query().graph().getAdjacentDescendants(id("oa1")), "pc2");
            assertIdOfNameNotInLongArray(pap.query().graph().getAdjacentDescendants(id("oa1")), "pc1");
            assertIdOfNameNotInLongArray(pap.query().graph().getAdjacentAscendants(id("pc1")), "oa1");
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua2", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua3", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua4", ids("ua1", "ua2", "ua3"));

            pap.runTx(tx -> {
                pap.modify().graph().deassign(id("ua4"), ids("ua1"));
                pap.modify().graph().deassign(id("ua4"), ids("ua2"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().deassign(id("ua4"), ids("ua3"));
                throw new PMException("");
            }));

            assertFalse(pap.query().graph().isAscendant(id("ua4"), id("ua1")));
            assertFalse(pap.query().graph().isAscendant(id("ua4"),id( "ua2")));
            assertTrue(pap.query().graph().isAscendant(id("ua4"), id("ua3")));
        }

        @Test
        void testOneDescendantIsAlreadyDeassigned() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua2", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua3", ids("ua1", "ua2"));

            pap.modify().graph().deassign(id("ua3"), ids("pc1", "ua1"));

            assertIdOfNameNotInLongArray(pap.query().graph().getAdjacentDescendants(id("ua3")), "pc1");
            assertIdOfNameNotInLongArray(pap.query().graph().getAdjacentDescendants(id("ua3")), "ua1");
        }
    }

    @Nested
    class AssociateTest {

        @Test
        void testUANodeDoesNotExistException() {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet()));
        }

        @Test
        void testTargetNodeDoesNotExistException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));

            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet()));
        }

        @Test
        void testAssignmentExistsDoesNotThrowException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua2", ids("ua1"));
            assertDoesNotThrow(() -> pap.modify().graph().associate(id("ua2"), id("ua1"), new AccessRightSet()));
        }

        @Test
        void testUnknownAccessRightException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            assertThrows(UnknownAccessRightException.class,
                    () -> pap.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet("read")));
            pap.modify().operations().setResourceAccessRights(new AccessRightSet("read"));
            assertThrows(UnknownAccessRightException.class,
                    () -> pap.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet("write")));
            assertDoesNotThrow(() -> pap.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet("read")));
            assertDoesNotThrow(() -> pap.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet(
                WC_ALL)));
            assertDoesNotThrow(() -> pap.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet(
                WC_RESOURCE)));
            assertDoesNotThrow(() -> pap.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet(
                WC_RESOURCE)));
        }

        @Test
        void testInvalidAssociationException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua2", ids("ua1"));
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.modify().graph().createObjectAttribute("oa2", ids("pc1"));

            assertThrows(
                    InvalidAssociationException.class,
                    () -> pap.modify().graph().associate(id("ua2"), id("pc1"), new AccessRightSet()));
            assertThrows(InvalidAssociationException.class,
                    () -> pap.modify().graph().associate(id("oa1"), id("oa2"), new AccessRightSet()));
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));

            pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));
            pap.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet("read"));

            assertEquals(
                    new Association(id("ua1"), id("oa1"), new AccessRightSet("read")),
                    pap.query().graph().getAssociationsWithSource(id("ua1")).iterator().next()
            );
            assertEquals(
                    new Association(id("ua1"), id("oa1"), new AccessRightSet("read")),
                    pap.query().graph().getAssociationsWithTarget(id("oa1")).iterator().next()
            );
        }

        @Test
        void testOverwriteSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));

            pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));
            pap.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet("read"));

            Collection<Association> assocs = pap.query().graph().getAssociationsWithSource(id("ua1"));
            Association assoc = assocs.iterator().next();
            assertEquals(id("ua1"), assoc.getSource());
            assertEquals(id("oa1"), assoc.getTarget());
            assertEquals(new AccessRightSet("read"), assoc.getAccessRightSet());

            pap.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet("read", "write"));

            assocs = pap.query().graph().getAssociationsWithSource(id("ua1"));
            assoc = assocs.iterator().next();
            assertEquals(id("ua1"), assoc.getSource());
            assertEquals(id("oa1"), assoc.getTarget());
            assertEquals(new AccessRightSet("read", "write"), assoc.getAccessRightSet());
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua2", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua3", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua4", ids("ua1", "ua2", "ua3"));

            pap.runTx(tx -> {
                pap.modify().graph().associate(id("ua4"), id("ua1"), new AccessRightSet("*"));
                pap.modify().graph().associate(id("ua4"), id("ua2"), new AccessRightSet("*"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().associate(id("ua4"), id("ua3"), new AccessRightSet("*"));
                throw new PMException("");
            }));

            assertTrue(pap.query().graph().getAssociationsWithSource(id("ua4")).containsAll(List.of(
                    new Association(id("ua4"), id("ua1"), new AccessRightSet("*")),
                    new Association(id("ua4"), id("ua2"), new AccessRightSet("*"))
            )));
            assertFalse(pap.query().graph().getAssociationsWithSource(id("ua4")).contains(
                    new Association(id("ua4"), id("ua3"), new AccessRightSet("*"))
            ));
        }
    }

    @Nested
    class DissociateTest {

        @Test
        void testUANodeDoesNotExistException() {
            assertThrows(NodeDoesNotExistException.class, () -> pap.modify().graph().dissociate(id("ua1"), id("oa1")));
        }

        @Test
        void testTargetNodeDoesNotExistException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));

            assertThrows(NodeDoesNotExistException.class, () -> pap.modify().graph().dissociate(id("ua1"), id("oa2")));
        }

        @Test
        void testAssociationDoesNotExistDoesNotThrowException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));

            assertDoesNotThrow(() -> pap.modify().graph().dissociate(id("ua1"), id("oa1")));
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().associate(id("ua1"), id("oa1"), new AccessRightSet());

            pap.modify().graph().dissociate(id("ua1"), id("oa1"));

            assertTrue(pap.query().graph().getAssociationsWithSource(id("ua1")).isEmpty());
            assertTrue(pap.query().graph().getAssociationsWithTarget(id("oa1")).isEmpty());
        }

        @Test
        void testTx() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua2", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua3", ids("pc1"));
            pap.modify().graph().createUserAttribute("ua4", ids("ua1", "ua2", "ua3"));

            pap.modify().graph().associate(id("ua4"), id("ua1"), new AccessRightSet("*"));
            pap.modify().graph().associate(id("ua4"), id( "ua2"), new AccessRightSet("*"));
            pap.modify().graph().associate(id("ua4"), id("ua3"), new AccessRightSet("*"));

            pap.runTx(tx -> {
                pap.modify().graph().dissociate(id("ua4"), id("ua1"));
                pap.modify().graph().dissociate(id("ua4"), id("ua2"));
            });
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().graph().dissociate(id("ua4"), id("ua3"));
                throw new PMException("");
            }));

            assertFalse(pap.query().graph().getAssociationsWithSource(id("ua4")).containsAll(List.of(
                    new Association(id("ua4"), id("ua1"), new AccessRightSet("*")),
                    new Association(id("ua4"), id("ua2"), new AccessRightSet("*"))
            )));
            assertTrue(pap.query().graph().getAssociationsWithSource(id("ua4")).contains(
                    new Association(id("ua4"), id("ua3"), new AccessRightSet("*"))
            ));
        }
    }
}