package gov.nist.csd.pm.pip.prohibitions;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.memory.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProhibitionsSerializerTest {

    private Prohibitions dao;

    @BeforeEach
    void setUp() throws PMException {
        dao = new MemProhibitions();

        Prohibition prohibition = new Prohibition.Builder("prohibition1", "123", new OperationSet("read"))
                .setIntersection(false)
                .addContainer("1234", true)
                .build();

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

        assertTrue(prohibition.getContainers().containsKey("1234"));
        assertTrue(prohibition.getContainers().get("1234"));
    }
}
