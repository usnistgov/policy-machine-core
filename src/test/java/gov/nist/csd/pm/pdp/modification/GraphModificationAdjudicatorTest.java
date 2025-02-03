package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.common.op.graph.*;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.UnauthorizedException;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.common.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.common.op.graph.GraphOp.*;
import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static gov.nist.csd.pm.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.*;

class GraphModificationAdjudicatorTest {

    PAP pap;
    PDP pdp;
    EPP epp;

    TestEventSubscriber testEventProcessor;
    GraphModificationAdjudicator ok;
    GraphModificationAdjudicator fail;


    @BeforeEach
    void setup() throws PMException {
        pap = new TestPAP();

        TestUserContext u1 = new TestUserContext("u1");

        pap.executePML(u1, """
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
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]
                associate "ua3" and "ua1" with ["*a"]
                associate "ua1" and "ua4" with ["*a"]
                associate "ua1" and "ua3" with ["*a"]
                
                create u "u1" in ["ua1", "ua3"]
                create u "u2" in ["ua2"]
                create o "o1" in ["oa1"]
                """);

        pdp = new PDP(pap);
        epp = new EPP(pdp, pap);

        testEventProcessor = new TestEventSubscriber();
        pdp.addEventSubscriber(testEventProcessor);

        ok = new GraphModificationAdjudicator(u1, pap, pdp, new PrivilegeChecker(pap));
        fail = new GraphModificationAdjudicator(new TestUserContext("u2"), pap, pdp, new PrivilegeChecker(pap));
    }

    @Test
    void createPolicyClass() throws PMException {
        assertDoesNotThrow(() -> ok.createPolicyClass("test"));
        assertEquals(
                new EventContext("u1", null, new CreatePolicyClassOp(), Map.of(NAME_OPERAND, "test")),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () -> fail.createPolicyClass("test"));
    }

    @Test
    void createUserAttribute() throws PMException {
        assertDoesNotThrow(() -> ok.createUserAttribute("test", ids("ua1")));
        assertEquals(
                new EventContext("u1", null, new CreateUserAttributeOp(), Map.of(NAME_OPERAND, "test", DESCENDANTS_OPERAND, List.of("ua1"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () -> fail.createUserAttribute("test", ids("ua1")));
    }

    @Test
    void createObjectAttribute() throws PMException {
        assertDoesNotThrow(() -> ok.createObjectAttribute("test", ids("oa1")));
        assertEquals(
                new EventContext("u1", null, new CreateObjectAttributeOp(), Map.of(NAME_OPERAND, "test", DESCENDANTS_OPERAND, List.of("oa1"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () ->  fail.createObjectAttribute("test", ids("oa1")));
    }

    @Test
    void createObject() throws PMException {
        assertDoesNotThrow(() -> ok.createObject("test", ids("oa1")));
        assertEquals(
                new EventContext("u1", null, new CreateObjectOp(), Map.of(NAME_OPERAND, "test", DESCENDANTS_OPERAND, List.of("oa1"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () ->  fail.createObject("test", ids("oa1")));
    }

    @Test
    void createUser() throws PMException {
        assertDoesNotThrow(() -> ok.createUser("test", ids("ua1")));
        assertEquals(
                new EventContext("u1", null, new CreateUserOp(), Map.of(NAME_OPERAND, "test", DESCENDANTS_OPERAND, List.of("ua1"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () -> fail.createUser("test", ids("ua1")));
    }

    @Test
    void setNodeProperties() throws PMException {
        assertDoesNotThrow(() -> ok.setNodeProperties(id("o1"), Map.of("a", "b")));
        assertEquals(
                new EventContext("u1", null, new SetNodePropertiesOp(), Map.of(NODE_OPERAND, "o1")),
                testEventProcessor.getEventContext()
        );
	    assertEquals(pap.query().graph().getNodeByName("o1").getProperties(), Map.of("a", "b"));

        assertThrows(UnauthorizedException.class, () -> fail.setNodeProperties(id("o1"), Map.of("a", "b")));
    }

    @Test
    void deleteNodeOk() throws PMException {
        assertDoesNotThrow(() -> ok.deleteNode(id("o1")));
        assertEquals(
                new EventContext("u1", null, new DeleteNodeOp(), Map.of(NODE_OPERAND, "o1", DESCENDANTS_OPERAND, List.of("oa1"))),
                testEventProcessor.getEventContext()
        );

        assertFalse(pap.query().graph().nodeExists("o1"));
    }

    @Test
    void deleteNodeFail() {
        assertThrows(UnauthorizedException.class, () -> fail.deleteNode(id("pc1")));
    }

    @Test
    void assign() throws PMException {
        assertDoesNotThrow(() -> ok.assign(id("o1"), ids("oa2")));
        assertEquals(
                new EventContext("u1", null, new AssignOp(), Map.of(ASCENDANT_OPERAND, "o1", DESCENDANTS_OPERAND, List.of("oa2"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().isAscendant(id("o1"), id("oa2")));

        assertThrows(UnauthorizedException.class, () -> fail.assign(id("o1"), ids("oa1")));
    }

    @Test
    void deassign() throws PMException {
        assertDoesNotThrow(() -> ok.deassign(id("u1"), ids("ua1")));
        assertEquals(
                new EventContext("u1", null, new DeassignOp(), Map.of(ASCENDANT_OPERAND, "u1", DESCENDANTS_OPERAND, List.of("ua1"))),
                testEventProcessor.getEventContext()
        );
        assertFalse(pap.query().graph().isAscendant(id("u1"), id("ua1")));

        assertThrows(UnauthorizedException.class, () -> fail.deassign(id("o1"), ids("oa1")));
    }

    @Test
    void associate() throws PMException {
        assertDoesNotThrow(() -> ok.associate(id("ua1"), id("ua3"), new AccessRightSet("assign")));
        assertEquals(
                new EventContext("u1", null, new AssociateOp(), Map.of(UA_OPERAND, "ua1", TARGET_OPERAND, "ua3")),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().getAssociationsWithSource(id("ua1"))
                .contains(new Association(id("ua1"), id("ua3"), new AccessRightSet("assign"))));

        assertThrows(UnauthorizedException.class, () -> fail.associate(id("ua1"), id("ua3"), new AccessRightSet("assign")));
    }

    @Test
    void dissociate() throws PMException {
        assertDoesNotThrow(() -> ok.dissociate(id("ua1"), id("ua3")));
        assertEquals(
                new EventContext("u1", null, new DissociateOp(), Map.of(UA_OPERAND, "ua1", TARGET_OPERAND, "ua3")),
                testEventProcessor.getEventContext()
        );
        assertFalse(pap.query().graph().getAssociationsWithSource(id("ua1"))
                .contains(new Association(id("ua1"), id("ua3"), new AccessRightSet("*a"))));

        assertThrows(UnauthorizedException.class, () -> fail.dissociate(id("ua1"), id("ua3")));
    }
}