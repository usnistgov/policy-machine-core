package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.node.NodeType;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.graph.relationship.Association;
import gov.nist.csd.pm.pap.obligation.EventContext;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.graph.*;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static gov.nist.csd.pm.pap.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.pap.op.graph.GraphOp.*;
import static org.junit.jupiter.api.Assertions.*;

class GraphModificationAdjudicatorTest {

    PAP pap;
    PDP pdp;
    EPP epp;

    TestEventProcessor testEventProcessor;
    GraphModificationAdjudicator ok;
    GraphModificationAdjudicator fail;


    @BeforeEach
    void setup() throws PMException {
        pap = new MemoryPAP();

        pap.executePML(new UserContext("u1"), """
                create pc "pc1"
                create pc "pc2"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create ua "ua3" in ["pc1"]
                create ua "ua4" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and "oa2" with ["*a"]
                associate "ua1" and ADMIN_POLICY_OBJECT with ["*a"]
                associate "ua3" and "ua1" with ["*a"]
                associate "ua1" and "ua4" with ["*a"]
                associate "ua1" and "ua3" with ["*a"]
                
                create u "u1" in ["ua1", "ua3"]
                create u "u2" in ["ua2"]
                create o "o1" in ["oa1"]
                """);

        pdp = new PDP(pap);
        epp = new EPP(pdp, pap);

        testEventProcessor = new TestEventProcessor();
        pdp.addEventListener(testEventProcessor);

        ok = new GraphModificationAdjudicator(new UserContext("u1"), pap, pdp);
        fail = new GraphModificationAdjudicator(new UserContext("u2"), pap, pdp);
    }

    @Test
    void createPolicyClass() throws PMException {
        assertDoesNotThrow(() -> ok.createPolicyClass("test"));
        assertEquals(
                new EventContext("u1", "", new CreatePolicyClassOp(), Map.of(NAME_OPERAND, "test")),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () -> fail.createPolicyClass("test"));
    }

    @Test
    void createUserAttribute() throws PMException {
        assertDoesNotThrow(() -> ok.createUserAttribute("test", List.of("ua1")));
        assertEquals(
                new EventContext("u1", "", new CreateUserAttributeOp(), Map.of(NAME_OPERAND, "test", DESCENDANTS_OPERAND, List.of("ua1"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () -> fail.createUserAttribute("test", List.of("ua1")));
    }

    @Test
    void createObjectAttribute() throws PMException {
        assertDoesNotThrow(() -> ok.createObjectAttribute("test", List.of("oa1")));
        assertEquals(
                new EventContext("u1", "", new CreateObjectAttributeOp(), Map.of(NAME_OPERAND, "test", DESCENDANTS_OPERAND, List.of("oa1"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () ->  fail.createObjectAttribute("test", List.of("oa1")));
    }

    @Test
    void createObject() throws PMException {
        assertDoesNotThrow(() -> ok.createObject("test", List.of("oa1")));
        assertEquals(
                new EventContext("u1", "", new CreateObjectOp(), Map.of(NAME_OPERAND, "test", DESCENDANTS_OPERAND, List.of("oa1"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () ->  fail.createObject("test", List.of("oa1")));
    }

    @Test
    void createUser() throws PMException {
        assertDoesNotThrow(() -> ok.createUser("test", List.of("ua1")));
        assertEquals(
                new EventContext("u1", "", new CreateUserOp(), Map.of(NAME_OPERAND, "test", DESCENDANTS_OPERAND, List.of("ua1"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () -> fail.createUser("test", List.of("ua1")));
    }

    @Test
    void setNodeProperties() throws PMException {
        assertDoesNotThrow(() -> ok.setNodeProperties("o1", Map.of("a", "b")));
        assertEquals(
                new EventContext("u1", "", new SetNodePropertiesOp(), Map.of(NAME_OPERAND, "o1", PROPERTIES_OPERAND, Map.of("a", "b"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().getNode("o1").getProperties().equals(Map.of("a", "b")));

        assertThrows(UnauthorizedException.class, () -> fail.setNodeProperties("o1", Map.of("a", "b")));
    }

    @Test
    void deleteNodeOk() throws PMException {
        assertDoesNotThrow(() -> ok.deleteNode("o1"));
        assertEquals(
                new EventContext("u1", "", new DeleteObjectOp(), Map.of(NAME_OPERAND, "o1", DESCENDANTS_OPERAND, Set.of("oa1"), TYPE_OPERAND, NodeType.O)),
                testEventProcessor.getEventContext()
        );
        assertDoesNotThrow(() -> ok.deleteNode("oa2"));
        assertEquals(
                new EventContext("u1", "", new DeleteObjectAttributeOp(), Map.of(NAME_OPERAND, "oa2", DESCENDANTS_OPERAND, Set.of("pc1"), TYPE_OPERAND, NodeType.OA)),
                testEventProcessor.getEventContext()
        );
        assertDoesNotThrow(() -> ok.deleteNode("ua4"));
        assertEquals(
                new EventContext("u1", "", new DeleteUserAttributeOp(), Map.of(NAME_OPERAND, "ua4", DESCENDANTS_OPERAND, Set.of("pc1"), TYPE_OPERAND, NodeType.UA)),
                testEventProcessor.getEventContext()
        );
        assertDoesNotThrow(() -> ok.deleteNode("pc2"));
        assertEquals(
                new EventContext("u1", "", new DeletePolicyClassOp(), Map.of(NAME_OPERAND, "pc2", TYPE_OPERAND, NodeType.PC, DESCENDANTS_OPERAND, Set.of())),
                testEventProcessor.getEventContext()
        );
        assertDoesNotThrow(() -> ok.deleteNode("u1"));
        assertEquals(
                new EventContext("u1", "", new DeleteUserOp(), Map.of(NAME_OPERAND, "u1", DESCENDANTS_OPERAND, Set.of("ua1", "ua3"), TYPE_OPERAND, NodeType.U)),
                testEventProcessor.getEventContext()
        );

        assertFalse(pap.query().graph().nodeExists("o1"));
        assertFalse(pap.query().graph().nodeExists("oa2"));
        assertFalse(pap.query().graph().nodeExists("ua4"));
        assertFalse(pap.query().graph().nodeExists("pc2"));
        assertFalse(pap.query().graph().nodeExists("u1"));
    }

    @Test
    void deleteNodeFail() {
        assertThrows(UnauthorizedException.class, () -> fail.deleteNode("pc1"));
        assertThrows(UnauthorizedException.class, () -> fail.deleteNode("ua1"));
        assertThrows(UnauthorizedException.class, () -> fail.deleteNode("oa1"));
        assertThrows(UnauthorizedException.class, () -> fail.deleteNode("u1"));
        assertThrows(UnauthorizedException.class, () -> fail.deleteNode("o1"));
    }

    @Test
    void assign() throws PMException {
        assertDoesNotThrow(() -> ok.assign("o1", List.of("oa2")));
        assertEquals(
                new EventContext("u1", "", new AssignOp(), Map.of(ASCENDANT_OPERAND, "o1", DESCENDANTS_OPERAND, List.of("oa2"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().isAscendant("o1", "oa2"));

        assertThrows(UnauthorizedException.class, () -> fail.assign("o1", List.of("oa1")));
    }

    @Test
    void deassign() throws PMException {
        assertDoesNotThrow(() -> ok.deassign("u1", List.of("ua1")));
        assertEquals(
                new EventContext("u1", "", new DeassignOp(), Map.of(ASCENDANT_OPERAND, "u1", DESCENDANTS_OPERAND, List.of("ua1"))),
                testEventProcessor.getEventContext()
        );
        assertFalse(pap.query().graph().isAscendant("u1", "ua1"));

        assertThrows(UnauthorizedException.class, () -> fail.deassign("o1", List.of("oa1")));
    }

    @Test
    void associate() throws PMException {
        assertDoesNotThrow(() -> ok.associate("ua1", "ua3", new AccessRightSet("assign")));
        assertEquals(
                new EventContext("u1", "", new AssociateOp(), Map.of(UA_OPERAND, "ua1", TARGET_OPERAND, "ua3", ARSET_OPERAND, new AccessRightSet("assign"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().getAssociationsWithSource("ua1").contains(new Association("ua1", "ua3", new AccessRightSet("assign"))));

        assertThrows(UnauthorizedException.class, () -> fail.associate("ua1", "ua3", new AccessRightSet("assign")));
    }

    @Test
    void dissociate() throws PMException {
        assertDoesNotThrow(() -> ok.dissociate("ua1", "ua3"));
        assertEquals(
                new EventContext("u1", "", new DissociateOp(), Map.of(UA_OPERAND, "ua1", TARGET_OPERAND, "ua3")),
                testEventProcessor.getEventContext()
        );
        assertFalse(pap.query().graph().getAssociationsWithSource("ua1").contains(new Association("ua1", "ua3", new AccessRightSet("*a"))));

        assertThrows(UnauthorizedException.class, () -> fail.dissociate("ua1", "ua3"));
    }
}