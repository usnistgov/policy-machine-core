package gov.nist.csd.pm.prohibitions;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.prohibitions.model.Prohibition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static gov.nist.csd.pm.prohibitions.model.Prohibition.SubjectType.USER;
import static gov.nist.csd.pm.prohibitions.model.Prohibition.SubjectType.USER_ATTRIBUTE;
import static org.junit.jupiter.api.Assertions.*;

class MemProhibitionsDAOTest {

    ProhibitionsDAO dao;

    @BeforeEach
    void setUp() {
        dao = new MemProhibitionsDAO();

        Prohibition prohibition = new Prohibition();
        prohibition.setName("prohibition1");
        prohibition.setSubject(new Prohibition.Subject(123, USER));
        prohibition.setOperations(new HashSet<>(Arrays.asList("read")));
        prohibition.setIntersection(false);
        prohibition.addNode(new NodeContext().id(1234).complement(true));

        dao.createProhibition(prohibition);
    }

    @Test
    void createProhibition() throws PMException {
        Prohibition p = null;
        assertThrows(IllegalArgumentException.class, () -> dao.createProhibition(p));

        Prohibition prohibition = new Prohibition();
        assertThrows(IllegalArgumentException.class, () -> dao.createProhibition(prohibition));
        prohibition.setName("p123");

        prohibition.setSubject(null);
        assertThrows(IllegalArgumentException.class, () -> dao.createProhibition(prohibition));
        assertThrows(IllegalArgumentException.class, () -> prohibition.setSubject(new Prohibition.Subject(123L, null)));
        assertThrows(IllegalArgumentException.class, () -> prohibition.setSubject(new Prohibition.Subject(0, USER)));
        assertThrows(IllegalArgumentException.class, () -> dao.createProhibition(prohibition));
        prohibition.setSubject(new Prohibition.Subject(123, USER));

        prohibition.setIntersection(false);
        prohibition.setOperations(new HashSet<>(Arrays.asList("read")));
        prohibition.addNode(new NodeContext().id(1234).complement(true));

        dao.createProhibition(prohibition);

        Prohibition p123 = dao.getProhibition("p123");
        assertEquals("p123", p123.getName());
        assertFalse(p123.isIntersection());
        assertEquals(p123.getOperations(), new HashSet<>(Arrays.asList("read")));
        assertEquals(p123.getNodes().get(0), new NodeContext().id(1234).complement(true));
    }

    @Test
    void getProhibitions() throws PMException {
        List<Prohibition> prohibitions = dao.getProhibitions();
        assertEquals(1, prohibitions.size());
    }

    @Test
    void getProhibition() throws PMException {
        assertThrows(PMException.class, () -> dao.getProhibition("abcd"));

        Prohibition prohibition = dao.getProhibition("prohibition1");
        assertEquals("prohibition1", prohibition.getName());
        assertEquals(123, prohibition.getSubject().getSubjectID());
        assertEquals(USER, prohibition.getSubject().getSubjectType());
        assertFalse(prohibition.isIntersection());
        assertEquals(prohibition.getOperations(), new HashSet<>(Arrays.asList("read")));
        assertEquals(prohibition.getNodes().get(0), new NodeContext().id(1234).complement(true));
    }

    @Test
    void updateProhibition() throws PMException {
        assertThrows(IllegalArgumentException.class, () -> dao.updateProhibition(null));
        Prohibition p = new Prohibition();
        assertThrows(IllegalArgumentException.class, () -> dao.updateProhibition(p));

        Prohibition prohibition = dao.getProhibition("prohibition1");
        prohibition.setName("prohibition123");
        prohibition.setSubject(new Prohibition.Subject(12345, USER_ATTRIBUTE));
        prohibition.setOperations(new HashSet<>(Arrays.asList("read", "write")));
        prohibition.setIntersection(true);
        prohibition.removeNode(1234);
        prohibition.addNode(new NodeContext().id(4321).complement(false));

        dao.updateProhibition(prohibition);

        prohibition = dao.getProhibition("prohibition123");
        assertTrue(prohibition.isIntersection());
        assertEquals("prohibition123", prohibition.getName());
        assertEquals(12345, prohibition.getSubject().getSubjectID());
        assertEquals(USER_ATTRIBUTE, prohibition.getSubject().getSubjectType());
        assertEquals(new HashSet<>(Arrays.asList("read", "write")), prohibition.getOperations());
        assertEquals(new NodeContext().id(4321).complement(false), prohibition.getNodes().get(0));
    }

    @Test
    void deleteProhibition() {
    }
}