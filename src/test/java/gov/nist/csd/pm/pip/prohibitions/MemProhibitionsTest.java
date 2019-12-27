package gov.nist.csd.pm.pip.prohibitions;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static gov.nist.csd.pm.pip.prohibitions.model.Prohibition.Subject.Type.USER;
import static gov.nist.csd.pm.pip.prohibitions.model.Prohibition.Subject.Type.USER_ATTRIBUTE;
import static org.junit.jupiter.api.Assertions.*;

class MemProhibitionsTest {

    Prohibitions dao;

    @BeforeEach
    void setUp() throws PMException {
        dao = new MemProhibitions();

        Prohibition prohibition = new Prohibition();
        prohibition.setName("prohibition1");
        prohibition.setSubject(new Prohibition.Subject(123, USER));
        prohibition.setOperations(new OperationSet("read"));
        prohibition.setIntersection(false);
        prohibition.addNode(new Prohibition.Node(1234, true));

        dao.add(prohibition);
    }

    @Test
    void createProhibition() throws PMException {
        Prohibition p = null;
        assertThrows(IllegalArgumentException.class, () -> dao.add(p));

        Prohibition prohibition = new Prohibition();
        assertThrows(IllegalArgumentException.class, () -> dao.add(prohibition));
        prohibition.setName("p123");

        prohibition.setSubject(null);
        assertThrows(IllegalArgumentException.class, () -> dao.add(prohibition));
        assertThrows(IllegalArgumentException.class, () -> prohibition.setSubject(new Prohibition.Subject(123L, null)));
        assertThrows(IllegalArgumentException.class, () -> prohibition.setSubject(new Prohibition.Subject(0, USER)));
        assertThrows(IllegalArgumentException.class, () -> dao.add(prohibition));
        prohibition.setSubject(new Prohibition.Subject(123, USER));

        prohibition.setIntersection(false);
        prohibition.setOperations(new OperationSet("read"));
        prohibition.addNode(new Prohibition.Node(1234, true));

        dao.add(prohibition);

        Prohibition p123 = dao.get("p123");
        assertEquals("p123", p123.getName());
        assertFalse(p123.isIntersection());
        assertEquals(new OperationSet("read"), p123.getOperations());
        assertEquals(new Prohibition.Node(1234, true), p123.getNodes().get(0));
    }

    @Test
    void getProhibitions() throws PMException {
        List<Prohibition> prohibitions = dao.getAll();
        assertEquals(1, prohibitions.size());
    }

    @Test
    void getProhibition() throws PMException {
        assertThrows(PMException.class, () -> dao.get("abcd"));

        Prohibition prohibition = dao.get("prohibition1");
        assertEquals("prohibition1", prohibition.getName());
        assertEquals(123, prohibition.getSubject().getSubjectID());
        assertEquals(USER, prohibition.getSubject().getSubjectType());
        assertFalse(prohibition.isIntersection());
        assertEquals(prohibition.getOperations(), new OperationSet("read"));
        assertEquals(new Prohibition.Node(1234, true), prohibition.getNodes().get(0));
    }

    @Test
    void updateProhibition() throws PMException {
        assertThrows(IllegalArgumentException.class, () -> dao.update(null));
        Prohibition p = new Prohibition();
        assertThrows(IllegalArgumentException.class, () -> dao.update(p));

        Prohibition prohibition = dao.get("prohibition1");
        prohibition.setName("prohibition123");
        prohibition.setSubject(new Prohibition.Subject(12345, USER_ATTRIBUTE));
        prohibition.setOperations(new OperationSet("read", "write"));
        prohibition.setIntersection(true);
        prohibition.removeNode(1234);
        prohibition.addNode(new Prohibition.Node(4321, false));

        dao.update(prohibition);

        prohibition = dao.get("prohibition123");
        assertTrue(prohibition.isIntersection());
        assertEquals("prohibition123", prohibition.getName());
        assertEquals(12345, prohibition.getSubject().getSubjectID());
        assertEquals(USER_ATTRIBUTE, prohibition.getSubject().getSubjectType());
        assertEquals(new OperationSet("read", "write"), prohibition.getOperations());
        assertEquals(new Prohibition.Node(4321, false), prohibition.getNodes().get(0));
    }

    @Test
    void deleteProhibition() {
    }
}