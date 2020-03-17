package gov.nist.csd.pm.pip.prohibitions;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pip.prohibitions.model.Prohibition.Subject.Type.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ProhibitionsSerializerTest {

    private Prohibitions dao;

    @BeforeEach
    void setUp() throws PMException {
        dao = new MemProhibitions();

        Prohibition prohibition = new Prohibition();
        prohibition.setName("prohibition1");
        prohibition.setSubject(new Prohibition.Subject("123", USER));
        prohibition.setOperations(new OperationSet("read"));
        prohibition.setIntersection(false);
        prohibition.addNode(new Prohibition.Node("1234", true));

        dao.add(prohibition);
    }

    @Test
    void testSerializer() throws PMException {
        String json = ProhibitionsSerializer.toJson(dao);
        Prohibitions deDao = ProhibitionsSerializer.fromJson(new MemProhibitions(), json);

        assertEquals(1, deDao.getAll().size());

        Prohibition prohibition = deDao.getAll().get(0);

        assertEquals("prohibition1", prohibition.getName());
        assertFalse(prohibition.isIntersection());
        assertEquals(new OperationSet("read"), prohibition.getOperations());
        assertEquals(new Prohibition.Node("1234", true), prohibition.getNodes().get(0));
    }
}