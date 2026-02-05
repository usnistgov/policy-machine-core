package gov.nist.csd.pm.core.pdp.modification;

import static gov.nist.csd.pm.core.pap.operation.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static gov.nist.csd.pm.core.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.graph.AssignOp;
import gov.nist.csd.pm.core.pap.operation.graph.AssociateOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateObjectAttributeOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateObjectOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreatePolicyClassOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateUserAttributeOp;
import gov.nist.csd.pm.core.pap.operation.graph.CreateUserOp;
import gov.nist.csd.pm.core.pap.operation.graph.DeassignOp;
import gov.nist.csd.pm.core.pap.operation.graph.DeleteNodeOp;
import gov.nist.csd.pm.core.pap.operation.graph.DissociateOp;
import gov.nist.csd.pm.core.pap.operation.graph.SetNodePropertiesOp;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
                
                associate "ua1" and "oa1" with ["admin:*"]
                associate "ua1" and "oa2" with ["admin:*"]
                associate "ua1" and PM_ADMIN_BASE_OA with ["admin:*"]
                associate "ua3" and "ua1" with ["admin:*"]
                associate "ua1" and "ua4" with ["admin:*"]
                associate "ua1" and "ua3" with ["admin:*"]
                
                create u "u1" in ["ua1", "ua3"]
                create u "u2" in ["ua2"]
                create o "o1" in ["oa1"]
                """);

        pdp = new PDP(pap);
        epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        testEventProcessor = new TestEventSubscriber();
        pdp.addEventSubscriber(testEventProcessor);

        ok = new GraphModificationAdjudicator(u1, pap, pdp);
        fail = new GraphModificationAdjudicator(new TestUserContext("u2"), pap, pdp);
    }

    @Test
    void createPolicyClass() throws PMException {
        assertDoesNotThrow(() -> ok.createPolicyClass("test"));
        assertEquals(
                new EventContext(new EventContextUser("u1"), new CreatePolicyClassOp().getName(), Map.of(NAME_PARAM.getName(), "test")),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () -> fail.createPolicyClass("test"));
    }

    @Test
    void createUserAttribute() throws PMException {
        assertDoesNotThrow(() -> ok.createUserAttribute("test", ids("ua1")));
        assertEquals(
                new EventContext(new EventContextUser("u1"), new CreateUserAttributeOp().getName(), Map.of(NAME_PARAM.getName(), "test", "descendants", ids("ua1"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () -> fail.createUserAttribute("test", ids("ua1")));
    }

    @Test
    void createObjectAttribute() throws PMException {
        assertDoesNotThrow(() -> ok.createObjectAttribute("test", ids("oa1")));
        assertEquals(
                new EventContext(new EventContextUser("u1"), new CreateObjectAttributeOp().getName(), Map.of(NAME_PARAM.getName(), "test", "descendants", ids("oa1"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () ->  fail.createObjectAttribute("test", ids("oa1")));
    }

    @Test
    void createObject() throws PMException {
        assertDoesNotThrow(() -> ok.createObject("test", ids("oa1")));
        assertEquals(
                new EventContext(new EventContextUser("u1"), new CreateObjectOp().getName(), Map.of(NAME_PARAM.getName(), "test", "descendants", ids("oa1"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () ->  fail.createObject("test", ids("oa1")));
    }

    @Test
    void createUser() throws PMException {
        assertDoesNotThrow(() -> ok.createUser("test", ids("ua1")));
        assertEquals(
                new EventContext(new EventContextUser("u1"), new CreateUserOp().getName(), Map.of(NAME_PARAM.getName(), "test", "descendants", ids("ua1"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().nodeExists("test"));

        assertThrows(UnauthorizedException.class, () -> fail.createUser("test", ids("ua1")));
    }

    @Test
    void setNodeProperties() throws PMException {
        assertDoesNotThrow(() -> ok.setNodeProperties(id("o1"), Map.of("a", "b")));
        assertEquals(
                new EventContext(new EventContextUser("u1"), new SetNodePropertiesOp().getName(), Map.of("id", id("o1"), "properties", Map.of("a", "b"))),
                testEventProcessor.getEventContext()
        );
	    assertEquals(pap.query().graph().getNodeByName("o1").getProperties(), Map.of("a", "b"));

        assertThrows(UnauthorizedException.class, () -> fail.setNodeProperties(id("o1"), Map.of("a", "b")));
    }

    @Test
    void deleteNodeOk() throws PMException {
        assertDoesNotThrow(() -> ok.deleteNode(id("o1")));

        EventContext expected = new EventContext(new EventContextUser("u1"), new DeleteNodeOp().getName(),
            new HashMap<>(Map.of(
                "id", id("o1"),
                "type", NodeType.O.toString(),
                "descendants", new ArrayList<>(ids("oa1"))
            ))
        );
        EventContext actual = testEventProcessor.getEventContext();
        assertEquals(expected, actual);

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
                new EventContext(new EventContextUser("u1"), new AssignOp().getName(), Map.of("ascendant", id("o1"), "descendants", ids("oa2"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().isAscendant(id("o1"), id("oa2")));

        assertThrows(UnauthorizedException.class, () -> fail.assign(id("o1"), ids("oa1")));
    }

    @Test
    void deassign() throws PMException {
        assertDoesNotThrow(() -> ok.deassign(id("u1"), ids("ua1")));
        assertEquals(
                new EventContext(new EventContextUser("u1"), new DeassignOp().getName(), Map.of("ascendant", id("u1"), "descendants", ids("ua1"))),
                testEventProcessor.getEventContext()
        );
        assertFalse(pap.query().graph().isAscendant(id("u1"), id("ua1")));

        assertThrows(UnauthorizedException.class, () -> fail.deassign(id("o1"), ids("oa1")));
    }

    @Test
    void associate() throws PMException {
        assertDoesNotThrow(() -> ok.associate(id("ua1"), id("ua3"), new AccessRightSet("admin:graph:assignment:ascendant:create")));
        assertEquals(
                new EventContext(new EventContextUser("u1"), new AssociateOp().getName(), Map.of("ua", id("ua1"), "target", id("ua3"), "arset", List.of("admin:graph:assignment:ascendant:create"))),
                testEventProcessor.getEventContext()
        );
        assertTrue(pap.query().graph().getAssociationsWithSource(id("ua1"))
                .contains(new Association(id("ua1"), id("ua3"), new AccessRightSet("admin:graph:assignment:ascendant:create"))));

        assertThrows(UnauthorizedException.class, () -> fail.associate(id("ua1"), id("ua3"), new AccessRightSet("admin:graph:assignment:ascendant:create")));
    }

    @Test
    void dissociate() throws PMException {
        assertDoesNotThrow(() -> ok.dissociate(id("ua1"), id("ua3")));
        assertEquals(
                new EventContext(new EventContextUser("u1"), new DissociateOp().getName(), Map.of("ua", id("ua1"), "target", id("ua3"))),
                testEventProcessor.getEventContext()
        );
        assertFalse(pap.query().graph().getAssociationsWithSource(id("ua1"))
                .contains(new Association(id("ua1"), id("ua3"), new AccessRightSet("admin:*"))));

        assertThrows(UnauthorizedException.class, () -> fail.dissociate(id("ua1"), id("ua3")));
    }
}