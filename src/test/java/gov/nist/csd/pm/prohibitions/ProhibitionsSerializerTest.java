package gov.nist.csd.pm.prohibitions;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.prohibitions.model.Prohibition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static gov.nist.csd.pm.prohibitions.model.Prohibition.Subject.Type.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ProhibitionsSerializerTest {

    ProhibitionsDAO dao;

    @BeforeEach
    void setUp() throws PMException {
        dao = new MemProhibitionsDAO();

        Prohibition prohibition = new Prohibition();
        prohibition.setName("prohibition1");
        prohibition.setSubject(new Prohibition.Subject(123, USER));
        prohibition.setOperations(new HashSet<>(Arrays.asList("read")));
        prohibition.setIntersection(false);
        prohibition.addNode(new Prohibition.Node(1234, true));

        dao.createProhibition(prohibition);
    }

    @Test
    void testSerializer() throws PMException {
        String json = ProhibitionsSerializer.toJson(dao);
        ProhibitionsDAO deDao = ProhibitionsSerializer.fromJson(new MemProhibitionsDAO(), json);

        assertEquals(1, deDao.getProhibitions().size());

        Prohibition prohibition = deDao.getProhibitions().get(0);

        assertEquals("prohibition1", prohibition.getName());
        assertFalse(prohibition.isIntersection());
        assertEquals(new HashSet<>(Arrays.asList("read")), prohibition.getOperations());
        assertEquals(new Prohibition.Node(1234, true), prohibition.getNodes().get(0));

    }
}